package fr.iocean.asso.service.dto;

import fr.iocean.asso.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserLightDTO {

    private Long id;

    private String firstName;

    private String lastName;

    public UserLightDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserLightDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLightDTO{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + 
            "}";
    }
}
