package se.taekwondointernship.data.models.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonForm {
    @NotBlank(message = "Ditt förnamn kan inte vara tomt.")
    private String firstName;

    @NotBlank(message = "Ditt efternamn kan inte vara tomt.")
    private String lastName;

    @NotBlank(message = "Om du inte har något eget telefonnummer kan du skriva din kontaktpersons nummer.")
    private String phoneNumber;

    @NotBlank(message = "Om du är vuxen kan du skriva ditt eget eller någon näraståendes namn.")
    private String parentName;

    @NotBlank(message = "Om du är vuxen kan du skriva ditt eget eller någon näraståendes nummer.")
    private String parentNumber;
}