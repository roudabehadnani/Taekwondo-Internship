package se.taekwondointernship.data.service;

import se.taekwondointernship.data.models.entity.Message;
import se.taekwondointernship.data.models.form.MessageForm;

public interface MessageService {
    Message createWelcome(MessageForm form);
    Message createNews(MessageForm form);
    Message editWelcome(MessageForm form);
    Message editNews(MessageForm form);
}
