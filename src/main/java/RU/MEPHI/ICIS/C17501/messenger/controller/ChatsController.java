package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.service.ChatService;
import RU.MEPHI.ICIS.C17501.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.errorMessage;

@RestController
@RequestMapping("/streams")
public class ChatsController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @GetMapping
    public Response getAllStreams(@RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                  @RequestHeader("offsetPages") Integer offsetPages,
                                  @RequestHeader("sizeOfPage") Integer sizeOfPage,
                                  @RequestHeader("pass") String password) {
        if (userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        return chatService.getAllChats(requesterTelephoneNumber, offsetPages, sizeOfPage);
    }

    @GetMapping("/{chatName}")
    public Response getAllStreamsByName(@RequestHeader("requester_authorization_number") String
                                                requesterTelephoneNumber,
                                        @PathVariable String chatName,
                                        @RequestHeader("pass") String password) {
        if (userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        return chatService.getAllChatsByName(requesterTelephoneNumber, chatName);
    }

    @GetMapping("/users/me/subscriptions")
    public Response getAllChatsSubscribed(@RequestHeader("requester_authorization_number")
                                                  String requesterTelephoneNumber,
                                          @RequestHeader("offsetPages") Integer offsetPages,
                                          @RequestHeader("sizeOfPage") Integer sizeOfPage,
                                          @RequestHeader("pass") String password) {
        if (userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        return chatService.getAllChatsSubscribed(requesterTelephoneNumber, offsetPages, sizeOfPage);
    }


    @PostMapping("/users/me/subscriptions/subscribe/{chatId}")
    public Response setUserSubscribedToStream(@PathVariable Long chatId,
                                              @RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                              @RequestHeader("pass") String password) {
        if (userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        return chatService.setUserSubscribedToStream(requesterTelephoneNumber, chatId);
    }

    @PostMapping("/create/{chatName}")
    public Response createStream(@PathVariable String chatName,
                                 @RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                 @RequestHeader("pass") String password) {
        if (userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password) != null) {
            return new Response(userService.checkCredentialsAndStatusInRequests(requesterTelephoneNumber, password), errorMessage);
        }
        return chatService.createStream(requesterTelephoneNumber, chatName);
    }
}

