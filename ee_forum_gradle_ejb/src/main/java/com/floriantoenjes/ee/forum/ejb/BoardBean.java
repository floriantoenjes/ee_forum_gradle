package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Board;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@LocalBean
public class BoardBean {

    @PersistenceContext
    private EntityManager em;

    public Board find(Long id) {
        return em.find(Board.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Board> findAll() {
        return em.createNamedQuery("Board.findAll").getResultList();
    }
}
