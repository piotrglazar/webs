package com.piotrglazar.webs.model;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.dto.UserDetailsDto;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserDetails;
import com.piotrglazar.webs.model.repositories.WebsUserRepository;
import com.piotrglazar.webs.util.OperationLogging;
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
    @OperationLogging(operation = "createUser")
    public WebsUser createUser(final String username, final String password) {
        final WebsUser websUser = WebsUser.builder()
                                    .username(username)
                                    .password(passwordEncoder.encode(password))
                                    .build();
        websUserRepository.save(websUser);
        return websUser;
    }

    @Override
    public WebsUser findUserByUsername(final String username) {
        return websUserRepository.findByUsername(username);
    }

    @Override
    public WebsUser getUserByUsername(final String username) {
        final WebsUser websUser = findUserByUsername(username);

        if (websUser == null) {
            throw new WebsUserNotFoundException(username);
        }
        return websUser;
    }

    @Override
    public WebsUser update(final WebsUser websUser) {
        return websUserRepository.saveAndFlush(websUser);
    }

    @Override
    public WebsUser findUserByEmail(final String email) {
        return websUserRepository.findByEmail(email);
    }

    @Override
    public WebsUser findUserByAccountId(final Long accountId) {
        return websUserRepository.findByAccountsId(accountId);
    }

    @Override
    public UserDetailsDto getUserDetails(final String username) {
        final WebsUser websUser = getUserByUsername(username);
        final WebsUserDetails details = websUser.getDetails();
        return new UserDetailsDto(websUser.getUsername(), websUser.getEmail(), details.getAddress(), details.getMemberSince());
    }
}
