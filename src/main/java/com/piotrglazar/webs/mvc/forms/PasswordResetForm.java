package com.piotrglazar.webs.mvc.forms;

import org.hibernate.validator.constraints.Email;

public class PasswordResetForm {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
