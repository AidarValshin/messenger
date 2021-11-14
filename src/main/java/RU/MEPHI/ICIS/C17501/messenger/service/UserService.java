package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.UserRepository;
import RU.MEPHI.ICIS.C17501.messenger.responce.AllUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserService {
    @Autowired
     UserRepository userRepository;

    public AllUsersResponse getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        ArrayList<UserDTO> userDTOS = new ArrayList<>(allUsers.size());
        for (User user : allUsers) {
            userDTOS.add(UserDTO.builder()
                    .photoUrl(user.getPhotoUrl()!=null? user.getPhotoUrl(): "")
                    .telephoneNumber(user.getTelephoneNumber())
                    .fullName(user.getFirstName() + " " + user.getSecondName())
                    .isAdmin(user.getRolesOfUserSet().stream().anyMatch(roleUser -> roleUser.getRole().getName().equalsIgnoreCase("admin")))
                    .isBlocked(user.getIsLocked())
                    .isDeleted(user.getIsDeleted())
                    .login(user.getLogin())
                    .dateOfBirth(user.getDateOfBirth())
                    .gender(user.getGender())
                    .build());
        }
        return new AllUsersResponse("", "success", userDTOS);
    }
}
