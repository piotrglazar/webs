package com.piotrglazar.webs.config;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.util.beans.DatabaseStatisticsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;

@Component
public class WebsPublicMetrics implements PublicMetrics {

    public static final String METRIC_PREFIX = "Webs";

    private final DatabaseStatisticsBean databaseStatisticsBean;
    private final Supplier<Date> dateSupplier;

    @Autowired
    public WebsPublicMetrics(DatabaseStatisticsBean databaseStatisticsBean, Supplier<Date> dateSupplier) {
        this.databaseStatisticsBean = databaseStatisticsBean;
        this.dateSupplier = dateSupplier;
    }

    @Override
    public Collection<Metric<?>> metrics() {
        final Date timestamp = dateSupplier.get();
        return Lists.newArrayList(new Metric<>(getMetricName("users"), databaseStatisticsBean.getNumberOfUsers(), timestamp),
                new Metric<Number>(getMetricName("accounts"), databaseStatisticsBean.getNumberOfAccounts(), timestamp));
    }

    private String getMetricName(String name) {
        return METRIC_PREFIX.concat(name);
    }
}
