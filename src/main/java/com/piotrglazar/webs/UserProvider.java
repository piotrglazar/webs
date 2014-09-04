package com.piotrglazar.webs;

import com.piotrglazar.webs.dto.UserDetailsDto;
import com.piotrglazar.webs.model.WebsUser;

public interface UserProvider {

    WebsUser createUser(String username, String password);

    WebsUser findUserByUsername(String username);

    WebsUser getUserByUsername(String username);

    WebsUser update(WebsUser websUser);

    WebsUser findUserByEmail(String email);

    WebsUser findUserByAccountId(Long accountId);

    UserDetailsDto getUserDetails(String username);
}
