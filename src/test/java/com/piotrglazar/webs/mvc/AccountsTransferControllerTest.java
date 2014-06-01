package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static com.piotrglazar.webs.commons.Utils.addCsrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class AccountsTransferControllerTest extends AbstractContextTest {

    @Test
    public void shouldTransferMoney() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/accountsTransfer/1/").session(authenticate)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", "1")
                .param("accountNumber", "def456")
                .param("integralPart", "5")
                .param("fractionalPart", "35")))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/accounts/1/"));
    }

    @Test
    public void shouldShowMoneyTransferForm() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/accountsTransfer/1/").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//input[@id = 'accountNumber']").exists())
            .andExpect(xpath("//input[@id = 'integralPart']").exists())
            .andExpect(xpath("//input[@id = 'fractionalPart']").exists());
    }

    @Test
    public void shouldShowErrorWhenTryingToTransferNegativeAmountOfMoney() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/accountsTransfer/1/").session(authenticate)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", "1")
                .param("accountNumber", "def456")
                .param("integralPart", "-5")
                .param("fractionalPart", "-35")))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'integralPartError']").exists())
            .andExpect(xpath("//p[@id = 'fractionalPartError']").exists());
    }

    @Test
    public void shouldShowErrorWhenTryingToSend100FractionalUnits() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/accountsTransfer/1/").session(authenticate)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", "1")
                .param("accountNumber", "def456")
                .param("integralPart", "1")
                .param("fractionalPart", "100")))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'fractionalPartError']").exists());
    }

    @Test
    public void shouldShowErrorPageWhenThereAreInsufficientFunds() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/accountsTransfer/1/").session(authenticate)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", "1")
                .param("accountNumber", "def456")
                .param("integralPart", "1000")
                .param("fractionalPart", "0")))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@class = 'lead']").exists());
    }

    @Test
    public void shouldShowErrorWhenTryingToTransferMoneyFromOtherUserAccount() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/accountsTransfer/3/").session(authenticate)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", "3")
                .param("accountNumber", "def456")
                .param("integralPart", "1000")
                .param("fractionalPart", "0")))


        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/accounts"));
    }
}
