package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.UserBean;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class UserController {
    private List<User> users;

    private User user;

    private Long userId;

    @EJB
    private UserBean userBean;

    public void loadUsers() {
        users = userBean.findAll();
    }

    public void loadUser() {
        user = userBean.find(userId);
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
}
