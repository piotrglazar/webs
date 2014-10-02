package com.piotrglazar.webs.util.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;

public class WebsiteReader {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final WebTarget webTarget;

    public WebsiteReader(String target) {
        webTarget = ClientBuilder.newClient().target(target);
    }

    public String get() {
        try {
            final Response response = webTarget.request().get();
            return response.readEntity(String.class);
        } catch (ProcessingException | IllegalStateException e) {
            LOG.error("Failed to read web page", e);
            throw new WebsiteReadingException(webTarget.getUri());
        }
    }
}
