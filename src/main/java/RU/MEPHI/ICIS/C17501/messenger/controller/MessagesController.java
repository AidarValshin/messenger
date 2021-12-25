package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.NarrowDTO;
import RU.MEPHI.ICIS.C17501.messenger.db.dto.OutgoingMessageDTO;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.responce.message.MessageListResponse;
import RU.MEPHI.ICIS.C17501.messenger.service.ChatService;
import RU.MEPHI.ICIS.C17501.messenger.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
     *
     * @param targetChatId    id чата, куда отправляется сообщение
     * @param messageContent  тело сообщения
     * @param senderTelNumber телефонный номер отправителя
     * @return стандартный ответ сервера об успехе операции
     */
    @PostMapping("/messages")
    public Response sendMessage(@RequestParam(value = "to") Long targetChatId,
                                @RequestParam(value = "content") String messageContent,
                                @RequestHeader(value = "requester_authorization_number") String senderTelNumber) {

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
     *
     * @param anchorMessageId  id сообщения относительно которого осуществляется поиск
     * @param numBefore        количество сообщений идущих до id anchor
     * @param numAfter         количество сообщений идущих после id anchor
     * @param filterConditions объект фильтрации, представляет собой  массив JSON пар типа ключ значение
     * @return запрашиваемый список сообщений
     */
    @GetMapping("/messages")
    public MessageListResponse getMessages(@RequestParam(value = "anchor", required = false) Long anchorMessageId,
                                           @RequestParam(value = "num_before") Long numBefore,
                                           @RequestParam(value = "num_after") Long numAfter,
                                           @RequestParam(value = "narrow") String filterConditions) throws JsonProcessingException {

        List<NarrowDTO> filterConditionsList = objectMapper.readValue(filterConditions, new TypeReference<>() {});
        // Получаем список сообщений в соответствие с полученными условиями фильтрации
        List<Message> foundMessages = messageService.getMessages(anchorMessageId, numBefore, numAfter, filterConditionsList);

        // Конвертируем найденные сообщения в объекты для трансфера на фронт
        List<OutgoingMessageDTO> outgoingMessageDTOS = foundMessages
                .stream()
                .map(message -> new OutgoingMessageDTO(
                        message.getText(),
                        message.getIdMessage(),
                        message.getUser().getFirstName() + " " + message.getUser().getSecondName(),
                        message.getUser().getTelephoneNumber(),
                        System.currentTimeMillis())
                )
                .collect(Collectors.toList());

        return new MessageListResponse("", Response.successMessage, anchorMessageId, outgoingMessageDTOS);
    }

}
