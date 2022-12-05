package se.taekwondointernship.data.service;

import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.taekwondointernship.data.models.entity.Message;
import se.taekwondointernship.data.models.form.MessageForm;
import se.taekwondointernship.data.repository.MessageRepository;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class MessageServiceImpl implements MessageService{
    MessageRepository repository;
    ModelMapper modelMapper;
    public MessageServiceImpl(MessageRepository repository, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Message createWelcome(MessageForm form) {
        Message message = repository.save(modelMapper.map(form, Message.class));
        return extractWelcomeMessage(message);
    }
    @Override
    @Transactional
    public Message editWelcome(MessageForm form){
        Message message = modelMapper.map(form, Message.class);
        message.setMessageId(1);
        return extractWelcomeMessage(message);
    }

    private Message extractWelcomeMessage(Message message) {
        JSONObject jsonMessageDetails = new JSONObject();
        jsonMessageDetails.put("messageId", message.getMessageId());
        jsonMessageDetails.put("messageContent", message.getMessageContent());
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", jsonMessageDetails);
        try (FileWriter file = new FileWriter("welcomeMessage.json")){
            file.write(jsonMessage.toJSONString());
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return message;
    }
    @Override
    @Transactional
    public Message createNews(MessageForm form){
        Message message = repository.save(modelMapper.map(form, Message.class));
        return extractNewsMessage(message);
    }

    @Override
    @Transactional
    public Message editNews(MessageForm form){
        Message message = modelMapper.map(form, Message.class);
        message.setMessageId(1);
        return extractNewsMessage(message);
    }

    private Message extractNewsMessage(Message message){
        JSONObject jsonMessageDetails = new JSONObject();
        jsonMessageDetails.put("messageId", message.getMessageId());
        jsonMessageDetails.put("messageContent", message.getMessageContent());
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", jsonMessageDetails);
        try (FileWriter file = new FileWriter("newsMessage.json")){
            file.write(jsonMessage.toJSONString());
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return message;
    }
}
