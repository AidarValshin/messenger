package RU.MEPHI.ICIS.C17501.messenger.responce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public abstract class Response {
    @JsonProperty(value = "msg", required = true)
    private String message;
    @JsonProperty(value = "result", required = true)
    private String result;
}
