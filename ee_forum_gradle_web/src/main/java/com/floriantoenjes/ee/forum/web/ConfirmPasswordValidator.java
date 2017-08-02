package com.floriantoenjes.ee.forum.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "confirmPasswordValidator")
public class ConfirmPasswordValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password1Id = (String) component.getAttributes().get("password1");
        UIInput textInput = (UIInput) context.getViewRoot().findComponent(password1Id);
        String password1 = (String) textInput.getValue();

        String password2 = (String) value;

        if (password1 != null && password2 != null && !password1.equals(password2)) {
            throw new ValidatorException(new FacesMessage("passwords have to match"));
        }
    }
}
