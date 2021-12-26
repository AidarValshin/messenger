package RU.MEPHI.ICIS.C17501.messenger.db.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncomingMessageDTO implements Serializable {

    /**
     * Id чата, куда посылается сообщение
     */
    private Integer chatId;

    /**
     * Контент сообщения (строка символов UTF-16)
     */
    private String content;
}
