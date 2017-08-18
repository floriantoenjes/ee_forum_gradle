package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Message;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public Message find(Long messageId) {
        return em.find(Message.class, messageId);
    }

    public List<Message> findAllByReceiver(Long receiverId) {
        TypedQuery<Message> query = em.createQuery(
                "SELECT m FROM Message m WHERE m.receiver.id = :id", Message.class
        );
        query.setParameter("id", receiverId);

        return query.getResultList();
    }
}
