package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.dao.UserCredentials;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.projection.UserProjection;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.RoleRepository;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.UserRepository;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.user.AllUsersResponse;
import RU.MEPHI.ICIS.C17501.messenger.responce.user.UserResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.errorMessage;
import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.successMessage;

@Service
public class UserService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private MessageDigest md5;

    @SneakyThrows
    @PostConstruct
    private void postConstruct() {
        md5 = MessageDigest.getInstance("md5");
    }

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

    public User getUserByTelephoneNumber(String telNumber) {
        final Optional<User> user = userRepository.findById(telNumber);
        if (user.isEmpty()) {
            // TODO: логгируем
        }
        return user.get();
    }

    public Response createNewUser(String telephoneNumber, String login, String firstName,
                                  String secondName, Date dateOfBirth, String gender, String password) {
        if (userRepository.findById(telephoneNumber).isPresent()) {
            return new Response("User with this telephone number already exists", errorMessage);
        }
        if (!userRepository.findByLogin(login).isEmpty()) {
            return new Response("User with this login already exists", errorMessage);
        }
        md5.update(password.getBytes());
        byte[] digest = md5.digest();
        String passHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        UserCredentials userCredentials = UserCredentials.builder()
                .login(login)
                .password(passHash)
                .build();
        User user = User.builder()
                .telephoneNumber(telephoneNumber)
                .login(login)
                .firstName(firstName)
                .secondName(secondName)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .userCredentials(userCredentials)
                .isDeleted(false)
                .isLocked(false)
                .roles(roleRepository.findById(1L).stream().collect(Collectors.toSet()))
                .build();
        Set<ConstraintViolation<UserCredentials>> validationResultsUserCredentials = validator.validate(userCredentials);
        Set<ConstraintViolation<User>> validationResultsUser = validator.validate(user);
        if (!validationResultsUserCredentials.isEmpty() || !validationResultsUser.isEmpty()) {
            String validationResult = validationResultsUser.stream().map(r -> r.getPropertyPath() + ":" + r.getMessage()).collect(Collectors.joining(", ")) +
                    validationResultsUserCredentials.stream().map(r -> r.getPropertyPath() + ":" + r.getMessage()).collect(Collectors.joining(", "));
            return new Response(validationResult, errorMessage);
        }
        userRepository.save(user);
        return new Response("User is registered", successMessage);
    }
}
