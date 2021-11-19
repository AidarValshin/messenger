package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/streams")
public class ChatsController {
    @Autowired
    ChatService chatService;


    @GetMapping
    public Response getAllStreams(@RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.getAllUsers(requesterTelephoneNumber);
    }

    @GetMapping("/{telephoneNumber}")
    public Response getUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                             @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.getUserByTelephoneNumber(telephoneNumber,requesterTelephoneNumber);
    }


    @PostMapping    ("/{telephoneNumber}/block")
    public Response blockUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                               @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.blockUserByTelephoneNumber(telephoneNumber,requesterTelephoneNumber);
    }
}

