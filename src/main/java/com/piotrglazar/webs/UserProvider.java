package com.piotrglazar.webs;

import com.piotrglazar.webs.model.WebsUser;

public interface UserProvider {

    void createUser(String username, String password);

    WebsUser findUser(String username);
}
