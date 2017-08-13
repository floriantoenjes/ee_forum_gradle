package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Message;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class MessageBean {
    @PersistenceContext
    private EntityManager em;

    public MessageBean() {

    }

    public Message createMessage(Message message) {
        em.persist(message);
        return message;
    }
}
