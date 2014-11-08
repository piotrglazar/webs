package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.AbstractContextTest;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerContextTest extends AbstractContextTest {

    @Test
    public void shouldFetchExchangeRatesJson() throws Exception {
        // when
        mockMvc.perform(get("/resources/exchangeRates"))

        // then
            .andExpect(status().isOk())
            .andExpect(request().asyncResult(new StringContains("USD")));
    }
}
