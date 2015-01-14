package com.piotrglazar.webs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Supplier;

@Component
public class DateSupplier implements Supplier<Date> {

    private final Supplier<LocalDateTime> localDateSupplier;

    @Autowired
    public DateSupplier(final Supplier<LocalDateTime> localDateSupplier) {
        this.localDateSupplier = localDateSupplier;
    }

    @Override
    public Date get() {
        return convertLocalDateToDate(localDateSupplier.get());
    }

    private Date convertLocalDateToDate(LocalDateTime localDateTime) {
        final Instant instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
        return Date.from(instant);
    }
}
