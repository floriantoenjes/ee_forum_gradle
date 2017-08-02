package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.UserBean;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class RegisterController {

    private String confirmPassword;

    @Inject
    private SignInController signInController;

    @Inject
    private User user;

    @EJB
    private UserBean userBean;

    public String register() {
        if (!user.getPassword().equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("passwords have to match"));
            return "/register.xhtml";
        }

        user = userBean.register(user);
        signInController.setUser(user);

        return "pretty:home";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
