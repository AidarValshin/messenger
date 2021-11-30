package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.projection.UserProjection;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.UserRepository;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.user.AllUsersResponse;
import RU.MEPHI.ICIS.C17501.messenger.responce.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.errorMessage;
import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.successMessage;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Response getAllUsers(String requesterTelephoneNumber, int offsetPages, int sizeOfPage) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        List<UserProjection> allUsers = userRepository.findAllByProjection(PageRequest.of(offsetPages, sizeOfPage));
        ArrayList<UserDTO> userDTOS = new ArrayList<>(allUsers.size());
        for (UserProjection user : allUsers) {
            userDTOS.add(getUserDTO(user));
        }
        return new AllUsersResponse("", Response.successMessage, userDTOS);
    }

    public Response getAllUsersByLogin(String requesterTelephoneNumber, String login) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        List<UserProjection> allUsers = userRepository.findAllByProjectionAndByLogin(login);
        if (allUsers.isEmpty()) {
            return new Response("Invalid login '" + login + "'", errorMessage);
        }
        ArrayList<UserDTO> userDTOS = new ArrayList<>(allUsers.size());
        for (UserProjection user : allUsers) {
            userDTOS.add(getUserDTO(user));
        }
        return new AllUsersResponse("", Response.successMessage, userDTOS);
    }

    public Response getUserByTelephoneNumber(String telephoneNumber, String requesterTelephoneNumber) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        Optional<User> optionalUserByTelephoneNumber = userRepository.findById(telephoneNumber);
        if (optionalUserByTelephoneNumber.isPresent()) {
            User user = optionalUserByTelephoneNumber.get();
            UserDTO userDTO = getUserDTO(user);
            return new UserResponse("", successMessage, userDTO);
        }
        return new Response("Invalid user phone_number '" + telephoneNumber + "'", errorMessage);
    }

    public Response blockUserByTelephoneNumber(String targetTelephoneNumber, String requesterTelephoneNumber) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        User userRequester = optionalRequesterUserByTelephoneNumber.get();
        if (!isAdmin(userRequester)) {
            return new Response("You are not admin", errorMessage);
        }
        Optional<User> optionalTargetUserByTelephoneNumber = userRepository.findById(targetTelephoneNumber);
        if (optionalTargetUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid target user phone_number '" + targetTelephoneNumber + "'", errorMessage);
        }
        User userTarget = optionalTargetUserByTelephoneNumber.get();
        if (isAdmin(userTarget)) {
            return new Response("Target user is admin", errorMessage);
        }
        return setUserLocked(targetTelephoneNumber, requesterTelephoneNumber, userTarget);
    }

    private Response setUserLocked(String targetTelephoneNumber, String requesterTelephoneNumber,
                                   User userTarget) {
        userTarget.setIsLocked(true);
        User saved = userRepository.save(userTarget);
        if (saved.getIsLocked()) {
            return new Response("", successMessage);
        }
        return new Response("Database error. Please,contact support team. Requester "
                + requesterTelephoneNumber + ",target " + targetTelephoneNumber, errorMessage);
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch
                (roleUser -> roleUser.getName().equalsIgnoreCase("admin"));
    }

    private UserDTO getUserDTO(UserProjection user) {
        return UserDTO.builder()
                .photoUrl(user.getPhotoUrl() != null ? user.getPhotoUrl() : "")
                .telephoneNumber(user.getTelephoneNumber())
                .fullName(user.getFirstName() + " " + user.getSecondName())
                .isAdmin(user.getRoleName().stream().anyMatch
                        (roleUser -> roleUser.equalsIgnoreCase("admin")))
                .isBlocked(user.getIsBlocked())
                .isDeleted(user.getIsDeleted())
                .login(user.getLogin())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }


    private UserDTO getUserDTO(User user) {
        return UserDTO.builder()
                .photoUrl(user.getPhotoUrl() != null ? user.getPhotoUrl() : "")
                .telephoneNumber(user.getTelephoneNumber())
                .fullName(user.getFirstName() + " " + user.getSecondName())
                .isAdmin(isAdmin(user))
                .isBlocked(user.getIsLocked())
                .isDeleted(user.getIsDeleted())
                .login(user.getLogin())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

}
