package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message findOne(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        return null;
    }

    public Message createMessage(Long targetChatId, String messageContent, String senderTelNumber) {
        // Достаём отправителя сообщения

        //final User sender = userService.getUserByTelephoneNumber(senderTelNumber);

        // Создаём и сохраняем сообщение
        Message message = Message.builder()
                .idChat(targetChatId)
                .text(messageContent)
                .telephoneNumber(senderTelNumber)
                //.lastChangesDate(new Date())
                .isDeleted(false)
                .build();

        try {
            messageRepository.save(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // TODO: логгируем, что успешно создано
        return message;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findAllByIdChat(chatId);
    }

    public Message getMessageById(Long messageId) {
        return messageRepository.getById(messageId);
    }

    public List<Message> getAllMessagesForClient(String requesterTelephoneNumber) {
        return null;
    }

    public List<Message> findChatMessagesByContent(Long chatId, String content) {
        // TODO: поиск сообщений по краткому содержанию: ПОИСК ВНУТРИ ЧАТА
        // TODO: дёргаем метод MessageRepository, в котором join для нужных чатов и дальше
        //  поиск нужного контента
        return null;
    }

}
