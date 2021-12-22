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
public class NarrowDTO implements Serializable {

    /**
     * Признак (например, "stream" - чаты)
     */
    private String operator;

    /**
     * Значение операции получения (например, id чата)
     */
    private String operand;

}
