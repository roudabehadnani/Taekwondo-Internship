package se.taekwondointernship.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.taekwondointernship.data.models.dto.EmailDto;
import se.taekwondointernship.data.models.form.EmailForm;
import se.taekwondointernship.data.service.EmailService;

@RestController
@RequestMapping("/emailControl")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService){this.emailService = emailService;}

    @PostMapping
    public ResponseEntity<EmailDto> create(@RequestBody EmailForm form){
        System.out.println("### In Create Method ###");
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.create(form));
    }
    @PutMapping("/sender")
    public ResponseEntity<EmailDto> editSender(@RequestBody EmailForm form){
        return ResponseEntity.ok(emailService.editSender(form));
    }
    @PutMapping("/password")
    public ResponseEntity<EmailDto> editPassword(@RequestBody EmailForm form){
        return ResponseEntity.ok(emailService.editPassword(form));
    }
    @PutMapping("/name")
    public ResponseEntity<EmailDto> editSenderName(@RequestBody EmailForm form){
        return ResponseEntity.ok(emailService.editSenderName(form));
    }
    @PutMapping("/subject")
    public ResponseEntity<EmailDto> editSubject(@RequestBody EmailForm form){
        return ResponseEntity.ok(emailService.editSubject(form));
    }
}
