package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.BloombergNews;
import com.piotrglazar.webs.model.WebsNewsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class AdminControllerContextTest extends AbstractContextTest {

    @Autowired
    private WebsNewsRepository websNewsRepository;

    @Before
    public void logout() {
        Utils.logout(mockMvc);
    }

    @Test
    public void shouldOrdinaryUserNotAccessAdminPage() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME2, Settings.PASSWORD);

        // when
        mockMvc.perform(get("/admin").session(authenticate))

        // then
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void shouldAdminUserAccessAdminPage() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME, Settings.PASSWORD);

        // when
        mockMvc.perform(get("/admin").session(authenticate))

        // then
            .andExpect(status().isOk());
    }

    @Test
    public void shouldAdminUserSeeMoneyTransferAuditAndNewsImporters() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME, Settings.PASSWORD);

        // when
        mockMvc.perform(get("/admin").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//table[@id='moneyTransferAuditTable']").exists())
            .andExpect(xpath("//table[@id='newsImporterTable']").exists());
    }

    @Test
    public void shouldImportNews() throws Exception {
        // given
        final int newsImporterIndex = 0;
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME, Settings.PASSWORD);

        // when
        mockMvc.perform(get(String.format("/admin/importNews/%s/", newsImporterIndex)).session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//div[@class='alert alert-dismissable alert-success']").exists())
            .andExpect(xpath("//table[@id='moneyTransferAuditTable']").exists())
            .andExpect(xpath("//table[@id='newsImporterTable']").exists());

        // cleanup
        websNewsRepository.deleteAllNews(BloombergNews.getNewsName());
    }

    @Test
    public void shouldAccrueInterest() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME, Settings.PASSWORD);

        // when
        mockMvc.perform(get("/admin/interest").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//div[@class='alert alert-dismissable alert-success']").exists())
            .andExpect(xpath("//table[@id='moneyTransferAuditTable']").exists())
            .andExpect(xpath("//table[@id='newsImporterTable']").exists());
    }
}
