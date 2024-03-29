package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Chat;
import RU.MEPHI.ICIS.C17501.messenger.db.dao.ChatContact;
import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.ChatDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.ChatContactRepository;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.ChatRepository;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.UserRepository;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.chat.AllChatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.errorMessage;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatContactRepository chatContactRepository;

    @Autowired
    UserService userService;

    public Response getAllChats(String requesterTelephoneNumber, int offsetPages, int sizeOfPage) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        List<Chat> allChats = chatRepository.findAll(PageRequest.of(offsetPages, sizeOfPage))
                .getContent();
        ArrayList<ChatDTO> chatDTOS = new ArrayList<>(allChats.size());
        List<ChatContact> allChatsByTelephoneNumber = chatContactRepository.findAllByTelephoneNumber(requesterTelephoneNumber);
        for (Chat chat : allChats) {
            chatDTOS.add(getChatsDTO(chat, allChatsByTelephoneNumber));
        }
        return new AllChatsResponse("", Response.successMessage, chatDTOS);
    }

    public Response getAllChatsByName(String requesterTelephoneNumber, String chatName) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        List<Chat> allChats = chatRepository.findAllByChatName(chatName);
        ArrayList<ChatDTO> chatDTOS = new ArrayList<>(allChats.size());
        List<ChatContact> allChatsByTelephoneNumber = chatContactRepository.findAllByTelephoneNumber(requesterTelephoneNumber);
        for (Chat chat : allChats) {
            chatDTOS.add(getChatsDTO(chat, allChatsByTelephoneNumber));
        }
        return new AllChatsResponse("", Response.successMessage, chatDTOS);
    }

    public Response getAllChatsLikeByName(String requesterTelephoneNumber, String chatName, int offsetPages, int sizeOfPage) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        List<Chat> allChats = chatRepository.findByChatNameContaining(chatName, PageRequest.of(offsetPages, sizeOfPage));
        ArrayList<ChatDTO> chatDTOS = new ArrayList<>(allChats.size());
        List<ChatContact> allChatsByTelephoneNumber = chatContactRepository.findAllByTelephoneNumber(requesterTelephoneNumber);
        for (Chat chat : allChats) {
            chatDTOS.add(getChatsDTO(chat, allChatsByTelephoneNumber));
        }
        return new AllChatsResponse("", Response.successMessage, chatDTOS);
    }


    public Response getAllChatsSubscribed(String requesterTelephoneNumber, int offsetPages, int sizeOfPage) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        List<ChatContact> allChatsByTelephoneNumber = chatContactRepository.findAllByTelephoneNumber(requesterTelephoneNumber);
        ArrayList<ChatDTO> chatDTOS = new ArrayList<>(allChatsByTelephoneNumber.size());
        List<Chat> allChatsByIdChatIn = chatRepository.findAllByIdChatIn(allChatsByTelephoneNumber.stream()
                .map(ChatContact::getIdChat).collect(Collectors.toList()), PageRequest.of(offsetPages, sizeOfPage));
        for (Chat chat : allChatsByIdChatIn) {
            chatDTOS.add(getChatsDTO(chat));
        }
        return new AllChatsResponse("", Response.successMessage, chatDTOS);
    }

    public List<Long> getAllChatsSubscribed(String requesterTelephoneNumber) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return Collections.emptyList();
        }
        List<ChatContact> allChatsByTelephoneNumber = chatContactRepository.findAllByTelephoneNumber(requesterTelephoneNumber);
        return allChatsByTelephoneNumber.stream().map(ChatContact::getIdChat).collect(Collectors.toList());
    }

    public Response setUserSubscribedToStream(String requesterTelephoneNumber, Long streamId) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        Optional<Chat> optionalChat = chatRepository.findById(streamId);
        if (optionalChat.isEmpty()) {
            return new Response("Invalid chat'" + optionalChat + "'", errorMessage);
        }
        Chat chat = optionalChat.get();
        List<ChatContact> chatContactByTelephoneNumberAndChatId = chatContactRepository
                .findByTelephoneNumberAndIdChat(requesterTelephoneNumber, chat.getIdChat());
        if (!chatContactByTelephoneNumberAndChatId.isEmpty()) {
            return new Response("You are already subscribed", errorMessage);

        }
        ChatContact chatContact = ChatContact.builder()
                .idChat(chat.getIdChat())
                .telephoneNumber(requesterTelephoneNumber)
                .build();
        chatContactRepository.save(chatContact);
        return new Response("", Response.successMessage);
    }

    public Response setUserUnsubscribedToStream(String requesterTelephoneNumber, Long streamId) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }
        Optional<Chat> optionalChat = chatRepository.findById(streamId);
        if (optionalChat.isEmpty()) {
            return new Response("Invalid chat'" + optionalChat + "'", errorMessage);
        }
        Chat chat = optionalChat.get();
        List<ChatContact> chatContactByTelephoneNumberAndChatId = chatContactRepository
                .findByTelephoneNumberAndIdChat(requesterTelephoneNumber, chat.getIdChat());
        if (chatContactByTelephoneNumberAndChatId.isEmpty()) {
            return new Response("You are already unsubscribed", errorMessage);
        }
        chatContactRepository.deleteAll(chatContactByTelephoneNumberAndChatId);
        return new Response("", Response.successMessage);
    }

    public Response createStream(String requesterTelephoneNumber, String streamName) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        if (optionalRequesterUserByTelephoneNumber.isEmpty()) {
            return new Response("Invalid requester user phone_number '" + requesterTelephoneNumber + "'", errorMessage);
        }

        if (!chatRepository.findAllByChatName(streamName).isEmpty()) {
            return new Response("Stream with the name " + streamName + " already exists! Stream was not created...", errorMessage);
        }
        Chat chat = Chat.builder().chatName(streamName).build();
        Chat savedChat = chatRepository.save(chat);
        User user = optionalRequesterUserByTelephoneNumber.get();
        ChatContact builtChatContact = ChatContact.builder().idChat(savedChat.getIdChat()).telephoneNumber(user.getTelephoneNumber()).build();
        chatContactRepository.save(builtChatContact);
        return new Response("", Response.successMessage);
    }

    public Response deleteStream(String requesterTelephoneNumber, Long idChat) {
        Optional<User> optionalRequesterUserByTelephoneNumber = userRepository.findById(requesterTelephoneNumber);
        User userRequester = optionalRequesterUserByTelephoneNumber.get();
        if (!userService.isAdmin(userRequester)) {
            return new Response("You are not admin", errorMessage);
        }
        Optional<Chat> optionalChat = chatRepository.findById(idChat);
        if (optionalChat.isEmpty()) {
            return new Response("Stream with the id " + idChat + " doesn't  exist!", errorMessage);
        }
        chatRepository.delete(optionalChat.get());
        return new Response("", Response.successMessage);
    }

    private ChatDTO getChatsDTO(Chat chat, List<ChatContact> allChatsByTelephoneNumber) {
        return ChatDTO.builder()
                .photoUrl(chat.getPhotoUrl() != null ? chat.getPhotoUrl() : "")
                .id(chat.getIdChat())
                .lastMessageDate(chat.getLastMessageDate())
                .lastMessageId(chat.getLastMessageId() != null ? chat.getLastMessageId() : -1)
                .name(chat.getChatName() != null ? chat.getChatName() : "")
                .isSubscriber(allChatsByTelephoneNumber.stream().anyMatch(c -> Objects.equals(c.getIdChat(), chat.getIdChat())))
                .build();
    }

    private ChatDTO getChatsDTO(Chat chat) {
        return ChatDTO.builder()
                .photoUrl(chat.getPhotoUrl() != null ? chat.getPhotoUrl() : "")
                .id(chat.getIdChat())
                .lastMessageDate(chat.getLastMessageDate())
                .lastMessageId(chat.getLastMessageId() != null ? chat.getLastMessageId() : -1)
                .name(chat.getChatName() != null ? chat.getChatName() : "")
                .isSubscriber(true)
                .build();
    }

    /**
     * Метод получения имени чата по ID
     * @param chatId ID чата
     * @return название чата
     */
    public String getChatNameById(Long chatId) {
        String targetChatName = "";
        final Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            targetChatName = chat.get().getChatName();
        }
        return targetChatName;
    }

    /**
     * Метод получения всех подписчиков чата с указанным id
     * @param chatId id чата
     * @return список пользователей, входящих в чат
     */
    public List<User> getAllSubscribersByChatId(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findByIdChat(chatId);
        List<User> subscribersList = new ArrayList<>();
        if (chatOptional.isPresent()) {
            subscribersList = chatOptional.get()
                    .getChatContacts()
                    .stream()
                    .map(ChatContact::getUser)
                    .collect(Collectors.toList());
        }
        return subscribersList;
    }

}
