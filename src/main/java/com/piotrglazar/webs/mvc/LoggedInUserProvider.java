package com.piotrglazar.webs.mvc;

import com.google.common.collect.Sets;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class LoggedInUserProvider {

    public User getLoggedInUser() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        if (authentication != null)
            return (User) authentication.getPrincipal();
        else {
            return new User("user", "password", Sets.newHashSet());
        }
    }
}
