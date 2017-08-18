package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.MessageBean;
import com.floriantoenjes.ee.forum.ejb.UserBean;
import com.floriantoenjes.ee.forum.ejb.model.Message;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

@Named
@RequestScoped
public class UserController {
    private List<Message> messages;
    private List<User> users;

    private User user;

    private Long userId;

    private Long messageId;

    @Inject
    private Message message;

    @Inject
    private SignInController signInController;

    @EJB
    private MessageBean messageBean;

    @EJB
    private UserBean userBean;

    public void loadUsers() {
        users = userBean.findAll();
    }

    public String loadUser() {
        user = userBean.find(userId);
        if (user == null) {
            return "pretty:not-found";
        }
        return null;
    }

    public void loadMessages() {
        messages = messageBean.findAllByReceiver(signInController.getUser().getId());
    }

    public void loadMessage() {
        message = messageBean.find(messageId);
    }

    public String sendMessage() {
        message.setSender(signInController.getUser());
        message.setReceiver(userBean.find(userId));
        message.setCreated(new Date());

        messageBean.createMessage(message);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Message sent"));

        return "pretty:viewUser";
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
