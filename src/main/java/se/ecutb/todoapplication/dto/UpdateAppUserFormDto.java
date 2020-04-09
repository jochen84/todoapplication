package se.ecutb.todoapplication.dto;

import se.ecutb.todoapplication.constants.messages.ValidationMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateAppUserFormDto {

    @NotBlank(message = ValidationMessages.FIELD_REQUIRED_MESSAGE)
    @Size(min = 2, max = 255, message = ValidationMessages.NAME_MESSAGE)
    private String firstName;

    @NotBlank(message = ValidationMessages.FIELD_REQUIRED_MESSAGE)
    @Size(min = 2, max = 255, message = ValidationMessages.NAME_MESSAGE)
    private String lastName;

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
}
