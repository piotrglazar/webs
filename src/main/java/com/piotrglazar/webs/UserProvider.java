package com.piotrglazar.webs;

import com.piotrglazar.webs.model.WebsUser;

public interface UserProvider {

    WebsUser createUser(String username, String password);

    WebsUser findUser(String username);

    WebsUser update(WebsUser websUser);
}
