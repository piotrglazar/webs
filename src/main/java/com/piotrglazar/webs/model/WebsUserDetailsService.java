package com.piotrglazar.webs.model;

import com.piotrglazar.webs.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
class WebsUserDetailsService implements UserDetailsService {

    private UserProvider userProvider;

    @Autowired
    public WebsUserDetailsService(final UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final WebsUser websUser = userProvider.findUserByUsername(username);

        if (websUser == null) {
            throw new UsernameNotFoundException("There is no user ".concat(username));
        }
        final List<GrantedAuthority> authorities = getGrantedAuthorities(websUser);

        return new User(websUser.getUsername(), websUser.getPassword(), authorities);
    }

    private List<GrantedAuthority> getGrantedAuthorities(final WebsUser websUser) {
        return websUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
