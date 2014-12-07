package com.piotrglazar.webs.business;

import org.assertj.core.api.Assertions;
import org.springframework.mail.SimpleMailMessage;

public class SimpleMailMessageAssert {

    private final SimpleMailMessage that;

    private SimpleMailMessageAssert(final SimpleMailMessage that) {
        this.that = that;
    }

    public static SimpleMailMessageAssert assertThat(SimpleMailMessage that) {
        return new SimpleMailMessageAssert(that);
    }


    public SimpleMailMessageAssert hasMessageText(final String mailMessage) {
        Assertions.assertThat(that.getText()).isEqualTo(mailMessage);
        return this;
    }

    public SimpleMailMessageAssert hasSubject(final String subject) {
        Assertions.assertThat(that.getSubject()).isEqualTo(subject);
        return this;
    }

    public SimpleMailMessageAssert hasTo(final String... to) {
        Assertions.assertThat(that.getTo()).isEqualTo(to);
        return this;
    }
}
