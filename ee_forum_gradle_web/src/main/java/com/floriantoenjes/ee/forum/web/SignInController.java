package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.UserBean;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SignInController implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    private User user;

    @EJB
    private UserBean userBean;

    public String signin() {
        user = userBean.find(username, password).orElse(null);

        FacesContext fc = FacesContext.getCurrentInstance();
        if (user == null) {
            fc.addMessage("signinForm",
                    new FacesMessage("Username or password do not match"));
            return "signin.xhtml";
        }
        fc.addMessage(null, new FacesMessage("Successfully signed in"));
        return "pretty:home";
    }

    public String logout() {
        user = null;
        return "pretty:home";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
