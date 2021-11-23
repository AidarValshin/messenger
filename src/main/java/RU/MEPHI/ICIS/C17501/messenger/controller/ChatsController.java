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
    public Response getAllChatsSubscribed( @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.getAllChatsSubscribed(requesterTelephoneNumber);
    }


    @PostMapping("/users/me/subscriptions/subscribe/{chatId}")
    public Response setUserSubscribedToStream(@PathVariable Long chatId,
                                               @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.setUserSubscribedToStream(requesterTelephoneNumber, chatId);
    }

    @PostMapping("/create/{chatName}")
    public Response subscribeToStream(@PathVariable String chatName,
                                              @RequestHeader("requester_authorization_number") String requesterTelephoneNumber) {
        return chatService.createStream(requesterTelephoneNumber, chatName);
    }
}

