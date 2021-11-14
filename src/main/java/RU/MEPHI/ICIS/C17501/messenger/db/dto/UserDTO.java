package RU.MEPHI.ICIS.C17501.messenger.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {
    @JsonProperty("avatar_url")
    private String photoUrl;
    @JsonProperty("phone_number")
    private String telephoneNumber;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("is_admin")
    private Boolean isAdmin;
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    @JsonProperty("login")
    private String login;
    @JsonProperty("birth_date")
    private Date dateOfBirth;
    @JsonProperty("gender")
    private String gender;
}
