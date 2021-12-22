package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.NarrowDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.OutgoingMessageDTO;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.message.MessageListResponse;
import RU.MEPHI.ICIS.C17501.messenger.service.ChatService;
import RU.MEPHI.ICIS.C17501.messenger.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для организации обмена сообщений между клиентами.
 * Включает обработчики методов отправки сообщения(-ий), получение сообщения(-ий)
 */
@RestController
public class MessagesController {

    /**
     * Карта недоставленных в часты сообщений. Ключ - id чата, значение - сообщение.
     */
    private HashMap<Long, Message> undeliveredMessages = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Сервис для обработки функционала обмена сообщениями
     */
    MessageService messageService;

    /**
     * Сервис для связки сообщений с чатами
     */
    ChatService chatService;

    /**
     * Шаблон для отправки сообщений
     */
    //SimpMessagingTemplate messagingTemplate;

    /**
     * Класс для чтения JSON-форматированных данных
     */
    private ObjectMapper objectMapper;

    @Autowired
    public MessagesController(MessageService messageService,
                              ObjectMapper objectMapper) {
                                //SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        //this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Метод отправки сообщения
     * @param targetChatId id чата, куда отправляется сообщение
     * @param messageContent тело сообщения
     * @param senderTelNumber телефонный номер отправителя
     * @return стандартный ответ сервера об успехе операции
     */
    @PostMapping("/messages")
    public Response sendMessage(@RequestParam(value = "to") Long targetChatId,
                                @RequestParam(value = "content") String messageContent,
                                @RequestHeader(value = "requester_authorization_number") String senderTelNumber) {

        // Создаём сообщение в БД
        Message message = messageService.createMessage(targetChatId, messageContent, senderTelNumber);

        // Кладём полученное сообщение в "не доставленные"
        undeliveredMessages.put(message.getIdMessage(), message);

        // Логгируем успех операции
        logger.info("Message to chatId={} with content=\"{{}}\" successfully received from client",
                targetChatId, messageContent);

        return new Response("", Response.successMessage);
    }

    /**
     * Метод получения сообщений
     * @param anchorMessageId id сообщения относительно которого осуществляется поиск
     * @param numberOfMessagesBeforeAnchor количество сообщений идущих до id anchor
     * @param numberOfMessagesAfterAnchor количество сообщений идущих после id anchor
     * @param filterConditions объект фильтрации, представляет собой  массив JSON пар типа ключ значение
     * @return запрашиваемый список сообщений
     * @throws JsonProcessingException исключение, возникающее при попытке маппинга условий фильтрации в DTO
     */
    @GetMapping("/messages")
    public MessageListResponse getMessages(@RequestParam(value = "anchor") Long anchorMessageId,
                                                @RequestParam(value = "num_before") Integer numberOfMessagesBeforeAnchor,
                                                @RequestParam(value = "num_after") Integer numberOfMessagesAfterAnchor,
                                                @RequestParam(value = "narrow") String filterConditions)
            throws JsonProcessingException {

        // Получаем условия фильтрации
        String jsonFromFilterConditions = filterConditions.substring(1, filterConditions.length()-1);
        NarrowDTO narrowDTO = objectMapper.readValue(jsonFromFilterConditions, NarrowDTO.class);
        var operand = narrowDTO.getOperand();
        var operator = narrowDTO.getOperator();

        // В зависимости от операции и операнда получаем список сообщений
        List<Message> foundMessages = new ArrayList<>();
        if (operator.equals("stream")) { // TODO: stream - в ENUM или в константу
             foundMessages = messageService.getMessagesByChatId(Long.valueOf(operand));
        }
        // TODO: добавить САМУ ФИЛЬТРАЦИЮ
        // TODO: мб один раз загружать аватарку, зачем её гонять туда-сюда
        // TODO: добавить метод подгрузки картинки в UserController

        // TODO: sender_id = 7 ? - у нас id юзера - его моб. номер!)
        // Конвертируем найденные сообщения в объекты для трансфера на фронт
        final List<OutgoingMessageDTO> outgoingMessageDTOS = foundMessages
                .stream().map(message -> new OutgoingMessageDTO(message.getText(),
                                                                message.getIdMessage(),
                                                                message.getUser().getFirstName() + " " + message.getUser().getSecondName(),
                                                                message.getUser().getTelephoneNumber(),
                                                                new Timestamp(System.currentTimeMillis())))
                .collect(Collectors.toList());
        // logger.info("asdad"); // TODO: доставили столько-то таких-то сообщений

        return new MessageListResponse(anchorMessageId, outgoingMessageDTOS);
    }







    // ---- ДАЛЕЕ ИДЕТ ВЕБ-СОКЕТНОЕ БАРАХЛО

    /**
     * Метод для отправки пользователем сообщения в чат
     * @param chatMessage Сообщение для отправки
     * @param senderTelephoneNumber Получатель (номер получателя)
     *//*
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload MessageDTO chatMessage,
                            @Header(value = "requester_authorization_number") String senderTelephoneNumber) {
        // Достаем данные
        var content = chatMessage.getContent();
        var chatId = chatMessage.getChatId();

        logger.info("Message to chatId={} with content=\"{{}}\" successfully received from client", chatId, content);

        // Создаём сообщение в БД
        Message message = messageService.createMessage(chatMessage, senderTelephoneNumber);

        // Создаём целевую очередь чата
        final String destinationQueue = "/chat/" + chatId;

        // Указываем хэдеры, которые нужно пробросить
        Map<String, Object> headers = new HashMap<>();
        headers.put("subscription", 1);

        // Отправляем сообщение в очередь чата для получения его всеми пользователями чата
        messagingTemplate.convertAndSend(destinationQueue, message, headers);
    }

    *//**
     * Метод подписки пользователя на получение сообщений из чата
     * @param id id чата
     * @return Строка ответа
     *//*
    @SubscribeMapping("/chat/subscribe/{id}")
    public String subscribeToTopic(@DestinationVariable Long id) {
        String responseString = "Successfully subscribed!";
        // TODO: Вызываем какой-то сервис и указываем, что пользователь подписан на такой-то стрим
        logger.info("User with id {} successfully subscribed to channel /topic/public", id);

        return responseString;
    }

    *//**
     * Метод обработки исключений, возникающих во время обмена сообщениями.
     * Кладёт все исключения и ошибки, связанные с общением, в очередь.
     * @param exception возникшее исключение
     * @return текст исключения/ошибки
     *//*
    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        logger.info("Exception \" {} \" raised and delivered to /errors queue", exception.getMessage());
        return exception.getMessage();
    }*/

}
