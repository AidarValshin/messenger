package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UserService userService;


    @GetMapping
    public Response getAllUsers(@RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                @RequestHeader("offsetPages") Integer offsetPages,
                                @RequestHeader("sizeOfPage") Integer sizeOfPage) {
        return userService.getAllUsers(requesterTelephoneNumber,offsetPages,sizeOfPage);
    }

    @GetMapping("/{telephoneNumber}")
    public Response getUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                             @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return userService.getUserByTelephoneNumber(telephoneNumber, requesterTelephoneNumber);
    }

    @GetMapping("/byLogin")
    public Response getAllUsersByLogin(@RequestHeader("login") String login,
                                             @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return userService.getAllUsersByLogin(requesterTelephoneNumber,login);
    }


    @PostMapping("/{telephoneNumber}/block")
    public Response blockUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                               @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return userService.blockUserByTelephoneNumber(telephoneNumber, requesterTelephoneNumber);
    }
}

