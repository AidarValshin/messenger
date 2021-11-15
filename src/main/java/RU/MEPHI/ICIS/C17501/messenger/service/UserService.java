package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.UserRepository;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.user.AllUsersResponse;
import RU.MEPHI.ICIS.C17501.messenger.responce.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Response getAllUsers(String requesterTelephoneNumber) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        } else {
            List<User> allUsers = userRepository.findAll();
            ArrayList<UserDTO> userDTOS = new ArrayList<>(allUsers.size());
            for (User user : allUsers) {
                userDTOS.add(getUserDTO(user));
            }
            return new AllUsersResponse("", Response.successMessage, userDTOS);
        }
    }

    public Response getUserByTelephoneNumber(String telephoneNumber, String requesterTelephoneNumber) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        } else {
        Optional<User> optionalUserByTelephoneNumber = userRepository.findById(telephoneNumber);
        if (optionalUserByTelephoneNumber.isPresent()) {
            User user = optionalUserByTelephoneNumber.get();
            UserDTO userDTO = getUserDTO(user);
            return new UserResponse("", successMessage, userDTO);
        }
        return new Response("Invalid user phone_number '" + telephoneNumber + "'", errorMessage);
    }}

    public Response blockUserByTelephoneNumber(String targetTelephoneNumber, String requesterTelephoneNumber) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        } else {
            User userRequester = optionalRequesterUserByTelephoneNumber.get();
            if (userRequester.getRolesOfUserSet().stream().anyMatch
                    (roleUser -> roleUser.getRole().getName().equalsIgnoreCase("admin"))) {
                Optional<User> optionalTargetUserByTelephoneNumber = userRepository.findById(targetTelephoneNumber);
                return getResponseCheckTarget(targetTelephoneNumber, requesterTelephoneNumber, optionalTargetUserByTelephoneNumber);
            } else {
                return new Response("You are not admin", errorMessage);
            }
        }
    }

    private Response getResponseCheckTarget(String targetTelephoneNumber, String requesterTelephoneNumber,
                                            Optional<User> optionalTargetUserByTelephoneNumber) {
        if (optionalTargetUserByTelephoneNumber.isPresent()) {
            User userTarget = optionalTargetUserByTelephoneNumber.get();
            if (userTarget.getRolesOfUserSet().stream().anyMatch
                    (roleUser -> roleUser.getRole().getName().equalsIgnoreCase("admin"))) {
                return new Response("Target user is admin", errorMessage);
            } else {
                userTarget.setIsLocked(true);
                User saved = userRepository.save(userTarget);
                if (saved.getIsLocked()) {
                    return new Response("", successMessage);
                } else {
                    return new Response("Database error. Please,contact support team. Requester "
                            + requesterTelephoneNumber + ",target " + targetTelephoneNumber, errorMessage);
                }
            }
        } else {
            return new Response("Invalid target user phone_number '" + targetTelephoneNumber + "'", errorMessage);
        }
    }

    private UserDTO getUserDTO(User user) {
        return UserDTO.builder()
                .photoUrl(user.getPhotoUrl() != null ? user.getPhotoUrl() : "")
                .telephoneNumber(user.getTelephoneNumber())
                .fullName(user.getFirstName() + " " + user.getSecondName())
                .isAdmin(user.getRolesOfUserSet().stream().anyMatch
                        (roleUser -> roleUser.getRole().getName().equalsIgnoreCase("admin")))
                .isBlocked(user.getIsLocked())
                .isDeleted(user.getIsDeleted())
                .login(user.getLogin())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }
}
