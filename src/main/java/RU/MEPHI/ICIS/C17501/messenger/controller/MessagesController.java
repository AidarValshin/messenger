package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.NarrowDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.OutgoingMessageDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.metadata.Operator;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.message.MessageListResponse;
import RU.MEPHI.ICIS.C17501.messenger.service.ChatService;
import RU.MEPHI.ICIS.C17501.messenger.service.MessageService;
import RU.MEPHI.ICIS.C17501.messenger.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static RU.MEPHI.ICIS.C17501.messenger.responce.Response.errorMessage;

/**
 * Контроллер для организации обмена сообщений между клиентами.
 * Включает обработчики методов отправки сообщения(-ий), получение сообщения(-ий)
 */
@RestController
public class MessagesController {

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
     * Сервис для проверки авторизации
     */
    @Autowired
    UserService userService;
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
                                @RequestHeader(value = "requester_authorization_number") String senderTelNumber,
                                @RequestHeader("pass") String password ) {
        if(!userService.checkCredentialsInRequests(senderTelNumber,password)){
            return new Response("Invalid credentials", errorMessage);
        }
        // Сохраняем сообщение в БД
        Message message = messageService.createMessage(targetChatId, messageContent, senderTelNumber);

        // Логгируем успех операции
        logger.info("Message to chatId={} with content=\"{{}}\" successfully received from client",
                targetChatId, messageContent);

        return new Response("", Response.successMessage);
    }

    /**
     * Метод получения сообщений.В случае наличия поля anchor - производится пагинация,
     * в случае отсутствия поля anchor - получение последних num_before новых сообщений
     * @param anchorMessageId id сообщения относительно которого осуществляется поиск
     * @param numBefore количество сообщений идущих до id anchor
     * @param numAfter количество сообщений идущих после id anchor
     * @param filterConditions объект фильтрации, представляет собой  массив JSON пар типа ключ значение
     * @return запрашиваемый список сообщений
     * @throws JsonProcessingException исключение, возникающее при попытке маппинга условий фильтрации в DTO
     */
    @GetMapping("/messages")
    public Response getMessages(@RequestParam(value = "anchor", required = false) Long anchorMessageId,
                                                @RequestParam(value = "num_before") Long numBefore,
                                                @RequestParam(value = "num_after") Long numAfter,
                                                @RequestParam(value = "narrow") String filterConditions,
                                           @RequestHeader(value = "requester_authorization_number") String senderTelNumber,
                                           @RequestHeader("pass") String password )
            throws JsonProcessingException {
        if(!userService.checkCredentialsInRequests(senderTelNumber,password)){
            return new Response("Invalid credentials", errorMessage);
        }
        // Получаем условия фильтрации
        String jsonFromFilterConditions = filterConditions.substring(1, filterConditions.length()-1);
        NarrowDTO narrowDTO = objectMapper.readValue(jsonFromFilterConditions, NarrowDTO.class);
        var operand = narrowDTO.getOperand();
        var operator = narrowDTO.getOperator();

        List<OutgoingMessageDTO> outgoingMessageDTOS = new ArrayList<>();

        // В зависимости от операции и операнда получаем список сообщений
        if (Objects.equals(Operator.STREAM.operatorType, operator)) {
            List<Message> foundMessages = new ArrayList<>();
            if (anchorMessageId != null) {  // Если требуется пагинация
                // Получаем сообщения для требуемого чата
                Long chatId = Long.valueOf(operand);
                //foundMessages = messageService.getMessagesByChatId(chatId);\
                foundMessages = messageService.getAllInChatByWindow(chatId, anchorMessageId, numBefore, numAfter);

            } else {  // Если необходимо только получение новых сообщений для чата
                foundMessages = messageService.getNewMessagesByChatId(numBefore, Long.valueOf(operand));

            }

            // Конвертируем найденные сообщения в объекты для трансфера на фронт
            outgoingMessageDTOS = foundMessages
                    .stream()
                    .map(message -> new OutgoingMessageDTO(
                                    message.getText(),
                                    message.getIdMessage(),
                                    message.getUser().getFirstName() + " " + message.getUser().getSecondName(),
                                    message.getUser().getTelephoneNumber(),
                                    System.currentTimeMillis())
                        )
                    .collect(Collectors.toList());
        }
        // TODO: добавить САМУ ФИЛЬТРАЦИЮ
        // TODO: sender_id = 7 ? - у нас id юзера - его моб. номер!)

        return new MessageListResponse("", Response.successMessage, anchorMessageId, outgoingMessageDTOS);
    }

}
