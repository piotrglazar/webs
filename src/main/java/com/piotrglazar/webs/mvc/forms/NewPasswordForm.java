package com.piotrglazar.webs.mvc.forms;

import org.hibernate.validator.constraints.NotBlank;

public class NewPasswordForm {

    @NotBlank
    private String newPassword;

    @NotBlank
    private String repeatedNewPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatedNewPassword() {
        return repeatedNewPassword;
    }

    public void setRepeatedNewPassword(final String repeatedNewPassword) {
        this.repeatedNewPassword = repeatedNewPassword;
    }
}
