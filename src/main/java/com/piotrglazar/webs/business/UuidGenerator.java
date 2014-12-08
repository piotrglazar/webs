package com.piotrglazar.webs.business;

import com.piotrglazar.webs.UniqueIdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("UuidGenerator")
public class UuidGenerator implements UniqueIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
