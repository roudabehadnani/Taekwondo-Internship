package se.taekwondointernship.data.service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.taekwondointernship.data.models.dto.EmailDto;
import se.taekwondointernship.data.models.entity.Email;
import se.taekwondointernship.data.models.entity.MessageEntity;
import se.taekwondointernship.data.models.form.EmailForm;
import se.taekwondointernship.data.repository.EmailRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService{
    MessageServiceImpl messageService;
    EmailRepository repository;
    ModelMapper modelMapper;
    JSONArray emailList = new JSONArray();
    public EmailServiceImpl(MessageServiceImpl messageService, EmailRepository repository, ModelMapper modelMapper){
        this.messageService = messageService;
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public EmailDto create(EmailForm form) {
        Email email = repository.save(modelMapper.map(form, Email.class));
        printEmail(extractEmailInfo(email));
        return modelMapper.map(email, EmailDto.class);
    }

    @Override
    @Transactional
    public EmailDto editSender(EmailForm form) {
        try {
            List<Email> listOfEmailInfo = getFromExistingEmailJSON();
            Email email = modelMapper.map(form, Email.class);
            email.setPassword(listOfEmailInfo.get(0).getPassword());
            email.setSubject(listOfEmailInfo.get(0).getSubject());
            email.setId(1);
            email.setSenderName(listOfEmailInfo.get(0).getSenderName());
            printEmail(extractEmailInfo(email));
            return modelMapper.map(email, EmailDto.class);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public EmailDto editPassword(EmailForm form) {
        try {
            List<Email> listOfEmailInfo = getFromExistingEmailJSON();
            Email email = modelMapper.map(form, Email.class);
            email.setSender(listOfEmailInfo.get(0).getSender());
            email.setSubject(listOfEmailInfo.get(0).getSubject());
            email.setId(1);
            email.setSenderName(listOfEmailInfo.get(0).getSenderName());
            printEmail(extractEmailInfo(email));
            return modelMapper.map(email, EmailDto.class);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public EmailDto editSenderName(EmailForm form){
        try {
            List<Email> listOfEmailInfo = getFromExistingEmailJSON();
            Email email = modelMapper.map(form, Email.class);
            email.setSender(listOfEmailInfo.get(0).getSender());
            email.setSubject(listOfEmailInfo.get(0).getSubject());
            email.setId(1);
            email.setPassword(listOfEmailInfo.get(0).getPassword());
            printEmail(extractEmailInfo(email));
            return modelMapper.map(email, EmailDto.class);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public EmailDto editSubject(EmailForm form){
        try {
            List<Email> listOfEmailInfo = getFromExistingEmailJSON();
            Email email = modelMapper.map(form, Email.class);
            email.setSender(listOfEmailInfo.get(0).getSender());
            email.setSenderName(listOfEmailInfo.get(0).getSenderName());
            email.setId(1);
            email.setPassword(listOfEmailInfo.get(0).getPassword());
            printEmail(extractEmailInfo(email));
            return modelMapper.map(email, EmailDto.class);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject extractEmailInfo(Email email) {
        JSONObject jsonEmailDetails = new JSONObject();
        jsonEmailDetails.put("id", email.getId());
        jsonEmailDetails.put("sender", email.getSender());
        jsonEmailDetails.put("password", email.getPassword());
        jsonEmailDetails.put("senderName", email.getSenderName());
        return jsonEmailDetails;
    }

    @Override
    @Transactional(readOnly = true)
    public String getPassword() {
        try {
            List<Email> listOfEmailInfo = getFromExistingEmailJSON();
            return listOfEmailInfo.get(0).getPassword();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MessageEntity getEmailMessage() {
        return messageService.findMessage("emailMessage");
    }

    @Override
    @Transactional
    public void sending(String recipient, String url) {
        try {
            Email email = getFromExistingEmailJSON().get(0);

            MimeMessage msg = new MimeMessage(setUpEmail());
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(email.getSender(), email.getSenderName()));
            msg.setReplyTo(InternetAddress.parse(email.getSender(), false));
            msg.setSubject(email.getSubject(), "UTF-8");
            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageService.findMessage("emailMessage.json").getMessageContent());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(url);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(url);
            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);
            Transport.send(msg);
            System.out.println("Email Sent Successfully with attachment!");
        } catch (MessagingException | IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Session setUpEmail() {
        try {
            Email email = getFromExistingEmailJSON().get(0);
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email.getSender(), email.getPassword());
                }
            };
            return Session.getDefaultInstance(properties, auth);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void printEmail(JSONObject jsonEmailDetails) {
        try (FileWriter file = new FileWriter("email.json")){
            file.write(jsonEmailDetails.toJSONString());
            file.flush();
            emailList.clear();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<Email> getFromExistingEmailJSON() throws IOException, ParseException {
        List<Email> listOfEmailInfo = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("email.json");
        Object obj = jsonParser.parse(reader);
        JSONArray jsonEmailList = new JSONArray();
        jsonEmailList.add(obj);
        jsonEmailList.forEach(mbr -> listOfEmailInfo.add(parseJsonEmail((JSONObject) mbr)));
        return listOfEmailInfo;
    }

    private Email parseJsonEmail(JSONObject objectEmail){
        Integer id = Integer.parseInt(String.valueOf(objectEmail.get("id")));
        String sender = (String) objectEmail.get("sender");
        String password = (String) objectEmail.get("password");
        String senderName = (String) objectEmail.get("senderName");
        String subject = (String) objectEmail.get("subject");
        return new Email(id, sender, password, senderName, subject);
    }
}