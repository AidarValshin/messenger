package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "device")
@Entity
public class Device {

    @Id
    private String registrationId;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userDevices", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<User> devices = new HashSet<>();
}
