package se.taekwondointernship.data.models.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDto {
    private Integer id;
    private String sender;
    private String password;
    private String senderName;
    private String subject;
}
