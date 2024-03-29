package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Device;
import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.dao.UserCredentials;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.projection.UserProjection;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.DeviceRepository;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.RoleRepository;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.UserCredentialsRepository;
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
import java.util.*;
import java.util.stream.Collectors;

import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.errorMessage;
import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.successMessage;

@Service
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private MessageDigest md5;

    @SneakyThrows
    @PostConstruct
    private void postConstruct() {
        md5 = MessageDigest.getInstance("md5");
    }

    public Response getAllUsers(String requesterTelephoneNumber, int offsetPages,
                                int sizeOfPage, String password) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        List<UserProjection> allUsers = userRepository.findAllByProjection(PageRequest.of(offsetPages, sizeOfPage));

        Set<String> usersSet = new HashSet<>((int) (allUsers.size() / 1.5));
        ArrayList<UserDTO> userDTOS = new ArrayList<>((int) (allUsers.size() / 1.5));
        for (UserProjection user : allUsers) {
            if (user.getRoleName() != null && user.getRoleName().equalsIgnoreCase("admin")) {
                userDTOS.add(getUserDTO(user));
                usersSet.add(user.getTelephoneNumber());
            }
        }
        allUsers.removeIf(p -> !usersSet.add(p.getTelephoneNumber()));
        for (UserProjection user : allUsers) {
            userDTOS.add(getUserDTO(user));
        }
        return new AllUsersResponse("", Response.successMessage, userDTOS);
    }

    public Response getAllUsersByLogin(String requesterTelephoneNumber, String login, String password) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        List<UserProjection> allUsers = userRepository.findAllWithRolesByProjectionAndByLogin(login);
        if (allUsers.isEmpty()) {
            return new Response("Invalid login '" + login + "'", errorMessage);
        }
        ArrayList<UserDTO> userDTOS = new ArrayList<>(allUsers.size());
        for (UserProjection user : allUsers) {
            userDTOS.add(getUserDTO(user));
        }
        return new AllUsersResponse("", Response.successMessage, userDTOS);
    }

    public Response getUserByTelephoneNumber(String telephoneNumber,
                                             String requesterTelephoneNumber,
                                             String password) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        Optional<User> optionalUserByTelephoneNumber = userRepository.findById(telephoneNumber);
        if (optionalUserByTelephoneNumber.isPresent()) {
            User user = optionalUserByTelephoneNumber.get();
            UserDTO userDTO = getUserDTO(user);
            return new UserResponse("", successMessage, userDTO);
        }
        return new Response("Invalid user phone_number '" + telephoneNumber + "'", errorMessage);
    }

    public Response getUsersLikeByTelephoneNumber(String telephoneNumber,
                                                  String requesterTelephoneNumber,
                                                  String password,
                                                  int offsetPages,
                                                  int sizeOfPage) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        List<User> byTelephoneNumberContaining = userRepository.findByTelephoneNumberContaining(telephoneNumber, PageRequest.of(offsetPages, sizeOfPage));
        if (byTelephoneNumberContaining.isEmpty()) {
            return new Response("", errorMessage);
        }
        ArrayList<UserDTO> userDTOS = new ArrayList<>(byTelephoneNumberContaining.size());
        for (User user : byTelephoneNumberContaining) {
            userDTOS.add(getUserDTO(user));
        }
        return new AllUsersResponse("", successMessage, userDTOS);
    }

    public Response getUsersLikeByLogin(String login,
                                        String requesterTelephoneNumber,
                                        String password,
                                        int offsetPages,
                                        int sizeOfPage) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        List<User> byTelephoneNumberContaining = userRepository.findByLoginContaining(login, PageRequest.of(offsetPages, sizeOfPage));
        if (byTelephoneNumberContaining.isEmpty()) {
            return new Response("", errorMessage);
        }
        ArrayList<UserDTO> userDTOS = new ArrayList<>(byTelephoneNumberContaining.size());
        for (User user : byTelephoneNumberContaining) {
            userDTOS.add(getUserDTO(user));
        }
        return new AllUsersResponse("", successMessage, userDTOS);
    }

    public Response blockUserByTelephoneNumber(String targetTelephoneNumber,
                                               String requesterTelephoneNumber,
                                               String password) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
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
        if (userTarget.getIsLocked()) {
            return new Response("Target user already was locked", errorMessage);
        }
        return setUserLocked(targetTelephoneNumber, requesterTelephoneNumber, userTarget);
    }

    public Response deleteUserByTelephoneNumber(String targetTelephoneNumber,
                                               String requesterTelephoneNumber,
                                               String password) {
        if (checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
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
        if (userTarget.getIsDeleted()) {
            return new Response("Target user already was deleted", errorMessage);
        }
        return setUserDeleted(targetTelephoneNumber, requesterTelephoneNumber, userTarget);
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
    private Response setUserDeleted(String targetTelephoneNumber, String requesterTelephoneNumber,
                                   User userTarget) {
        userTarget.setIsDeleted(true);
        User saved = userRepository.save(userTarget);
        if (saved.getIsDeleted()) {
            return new Response("", successMessage);
        }
        return new Response("Database error. Please,contact support team. Requester "
                + requesterTelephoneNumber + ",target " + targetTelephoneNumber, errorMessage);
    }
    public boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch
                (roleUser -> roleUser.getName().equalsIgnoreCase("admin"));
    }

    private UserDTO getUserDTO(UserProjection user) {
        return UserDTO.builder()
                .photoUrl(user.getPhotoUrl() != null ? user.getPhotoUrl() : "")
                .telephoneNumber(user.getTelephoneNumber())
                .fullName(user.getFirstName() + " " + user.getSecondName())
                .isAdmin(user.getRoleName() != null && user.getRoleName().equalsIgnoreCase("admin"))
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


    public Response createNewUser(String telephoneNumber, String login, String firstName, String secondName,
                                  Date dateOfBirth, String gender, String password, String fcmRegistrationTokenId) {

        // Проверяем, зарегистрирован ли уже пользователь с указанным номером телефона
        if (userRepository.findById(telephoneNumber).isPresent()) {
            return new Response("User with this telephone number already exists", errorMessage);
        }

        // Проверяем уникальность указанного логина
        if (!userRepository.findAllByLogin(login).isEmpty()) {
            return new Response("User with this login already exists", errorMessage);
        }

        // Создаём пользователя, пароль храним с зашифрованном виде
        String passHash = getPassHash(password);
        UserCredentials userCredentials = UserCredentials.builder()
                .telephoneNumber(telephoneNumber)
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
                .userDevices(new HashSet<>())
                .build();

        // Создаём устройство для обмена сообщениями, добавляем его во владение создаваемого пользователя
        associateDeviceToUser(fcmRegistrationTokenId, user);

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

    public Response authorize(String telephoneNumber, String password, String fcmRegistrationTokenId) {
        // Проверяем, есть (и если есть, то в каком статусе) указанный пользователь
        Optional<UserCredentials> optionalUserCredentials = userCredentialsRepository.findById(telephoneNumber);
        if (optionalUserCredentials.isPresent()) {
            UserCredentials userCredentials = optionalUserCredentials.get();
            User user = userCredentials.getUser();
            if (user.getIsDeleted()) {
                return new Response("User is deleted", errorMessage);
            }
            if (user.getIsLocked()) {
                return new Response("User is locked", errorMessage);
            }
            // Проверяем, правильный ли пароль для данного пользователя
            if (getPassHash(password).equals(userCredentials.getPassword())) {
                // Если устройство новое, то добавляем его и ассоцииируем
                // Если устройство не новое, то ничего не делаем, подрузумевая всё ту же "СМС-авторизацию"
                associateDeviceToUser(fcmRegistrationTokenId, user);
                return new Response("Right credentials", successMessage);
            }
        }
        return new Response("Invalid credentials", errorMessage);
    }

    public String checkCredentialsAndStatusInRequests(String requesterTelephoneNumber, String password) {
        Optional<UserCredentials> optionalUserCredentials = userCredentialsRepository.findById(requesterTelephoneNumber);
        if (optionalUserCredentials.isEmpty()) {
            return "Invalid credentials";
        }
        UserCredentials userCredentials = optionalUserCredentials.get();
        User user = userCredentials.getUser();
        if (user.getIsLocked()) {
            return "User is locked";
        }
        if (user.getIsDeleted()) {
            return "User is deleted";
        }
        if (getPassHash(password).equals(userCredentials.getPassword())) {
            return null;
        }
        return "Invalid credentials";
    }

    private String getPassHash(String password) {
        md5.update(password.getBytes());
        byte[] digest = md5.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    /**
     * Метод для связывания пользователя с устройством
     * @param fcmRegistrationTokenId ID Firebase-токена устройства пользователя
     * @param user пользователь добавляемого устройства
     * @return true - если устройство с указанным токеном было проассоциировано с пользователем. Иначе - false.
     */
    private boolean associateDeviceToUser(String fcmRegistrationTokenId, User user) {
        if (isNewDeviceForUser(fcmRegistrationTokenId, user)) {
            // Создаём сущность устройства
            Device device = Device.builder()
                    .registrationId(fcmRegistrationTokenId)
                    .build();
            // Проверяем, зарегистрировано ли вообще устройство с указанным fcmRegistrationTokenId
            if (deviceRepository.findById(fcmRegistrationTokenId).isEmpty()) {
                // Регистрируем новое устройство
                deviceRepository.save(device);
            }
            // Ассоциируем новое устройство с пользователем
            user.getUserDevices().add(device);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Метод, проверяющий, ассоциирован ли пользователь с устройством, имеющим
     * указанный fcmRegistrationTokenId
     * @param fcmRegistrationTokenId уникальный регистрационный токен для конкретного устройства входа
     * @param user пользователь
     * @return true - если устройство входа с указанным токеном - новое для данного пользователя. Иначе - false
     */
    private boolean isNewDeviceForUser(String fcmRegistrationTokenId, User user) {
        Set<Device> userDevices = user.getUserDevices();
        return userDevices.stream()
                .noneMatch(device -> device.getRegistrationId().equals(fcmRegistrationTokenId));
    }

    /**
     * Utility-метод получения полного имени пользователя
     * @param user пользователь
     * @return полное имя пользователя
     */
    public String getFullUserName(User user) {
        return user.getFirstName() + " " + user.getSecondName();
    }

    public User getUserEntityByTelNumber(String telNumberOfUser) {
        return userRepository.getById(telNumberOfUser);
    }

}
