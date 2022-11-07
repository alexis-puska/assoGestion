package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.User;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLightDTO implements Serializable {

    private static final long serialVersionUID = -4766160031712544604L;

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String telephone;

    public UserLightDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
    }
}
