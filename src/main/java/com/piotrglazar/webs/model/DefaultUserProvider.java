package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class DefaultUserProvider implements UserProvider {

    private WebsUserRepository websUserRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserProvider(final WebsUserRepository websUserRepository, final PasswordEncoder passwordEncoder) {
        this.websUserRepository = websUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public WebsUser createUser(final String username, final String password) {
        final WebsUser websUser = new WebsUser(username, passwordEncoder.encode(password), Sets.newHashSet("USER"));
        websUserRepository.save(websUser);
        return websUser;
    }

    @Override
    public WebsUser findUser(final String username) {
        return websUserRepository.findByUsername(username);
    }
}
