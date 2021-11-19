package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/streams")
public class ChatsController {
    @Autowired
    ChatService chatService;


    @GetMapping
    public Response getAllStreams(@RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.getAllChats(requesterTelephoneNumber);
    }

    @GetMapping("/users/me/subscriptions")
    public Response getUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                             @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.getAllChatsSubscribed(requesterTelephoneNumber);
    }


    @PostMapping("/me/subscriptions/subscribe/{chatId}")
    public Response blockUserByTelephoneNumber(@PathVariable Long chatId,
                                               @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.setUserSubscribedToStream(requesterTelephoneNumber, chatId);
    }
}

