package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userDetails")
public class UserDetailsController {

    private final LoggedInUserProvider loggedInUserProvider;
    private final UserProvider userProvider;

    @Autowired
    public UserDetailsController(LoggedInUserProvider loggedInUserProvider, UserProvider userProvider) {
        this.loggedInUserProvider = loggedInUserProvider;
        this.userProvider = userProvider;
    }

    @RequestMapping
    public String userDetails(final Model model) {
        final String username = loggedInUserProvider.getLoggedInUserUsername();
        model.addAttribute("welcomeMessage", String.format("Welcome %s!", username));
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "userDetails");
        model.addAttribute("userDetails", userProvider.getUserDetails(username));

        return "userDetails";
    }
}
