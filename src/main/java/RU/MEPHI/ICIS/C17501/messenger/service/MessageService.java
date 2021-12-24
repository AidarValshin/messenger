package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import RU.MEPHI.ICIS.C17501.messenger.db.repo.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    /**
     * Метод получения списка n новых сообщений для чата с id=chatId
     * @param n какое количество новых сообщений нам нужно
     * @param chatId в каком чате (с каким id) искать
     * @return список из n сообщений из заданного chatId чата
     */
    public List<Message> getNewMessagesByChatId(Long n, Long chatId) {

        final Page<Message> lastMessages = messageRepository
                .findAllByIdChat(chatId, PageRequest.of(0, Math.toIntExact(n), Sort.by(Sort.Order.desc("lastChangesDate"))));
        // TODO: сделать только в репозитории так, чтобы ещё и по id искал
        return lastMessages.getContent();
    }

    public List<Message> getAllInChatByWindow(Long chatId, Long messageId, Long numBefore, Long numAfter) {
        return messageRepository.findAllInChatByWindow(chatId, messageId, numBefore, numAfter);
    }

    /**
     * Метод получения всех сообщений из чата
     * @param chatId id чата, из которого получаем сообщения
     * @return список сообщений чата
     */
    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findAllByIdChat(chatId);
    }

    /**
     * Метод получения сообщения по id
     * @param messageId id сообщения
     * @return сообщение, которое требовалось найти
     */
    public Message getMessageById(Long messageId) {
        return messageRepository.getById(messageId);
    }

    /**
     * Метод получения всех сообщений для пользователя с заданным телефонным номером
     * @param requesterTelephoneNumber номер пользователя
     * @return список всех сообщений для пользователя с данным номером
     */
    public List<Message> getAllMessagesForClient(String requesterTelephoneNumber) {
        return null;
    }

    /**
     * Поиск списка сообщений для данного чата по ключевому слову
     * @param chatId id чата, в котором ищем
     * @param content по какому ключевому слову ищем
     * @return список найденных сообщений по данному ключевому слову в данном чате
     */
    public List<Message> findChatMessagesByContent(Long chatId, String content) {
        // TODO: дёргаем метод из MessageRepository, в котором LIKE(content). !!!!!!!!!!!!!!!!!!!!!
        // TODO: поиск сообщений по краткому содержанию: ПОИСК ВНУТРИ ЧАТА
        // TODO: дёргаем метод MessageRepository, в котором join для нужных чатов и дальше
        //  поиск нужного контента
        return null;
    }

}
