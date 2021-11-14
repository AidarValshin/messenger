package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.responce.AllUsersResponse;
import RU.MEPHI.ICIS.C17501.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UserService userService;


    @GetMapping
    public AllUsersResponse getAllUsers() {
        return userService.getAllUsers();
    }
}

