package com.piotrglazar.webs.config;

import com.piotrglazar.webs.util.beans.DatabaseStatisticsBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.metrics.Metric;

import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;

import static com.piotrglazar.webs.config.WebsPublicMetrics.METRIC_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class WebsPublicMetricsTest {

    private static final Date ARBITRARY_DATE = new Date(1234567890);

    @Mock
    private Supplier<Date> dateSupplier;

    @Mock
    private DatabaseStatisticsBean databaseStatisticsBean;

    @InjectMocks
    private WebsPublicMetrics websPublicMetrics;

    @Test
    public void shouldCreateMetricsForUsersAndAccounts() {
        // given
        given(dateSupplier.get()).willReturn(ARBITRARY_DATE);
        given(databaseStatisticsBean.getNumberOfUsers()).willReturn(2L);
        given(databaseStatisticsBean.getNumberOfAccounts()).willReturn(10L);

        // when
        final Collection<Metric<?>> metrics = websPublicMetrics.metrics();

        // then
        assertThat(metrics).containsExactly(new Metric<>(METRIC_PREFIX.concat("users"), 2L, ARBITRARY_DATE),
                new Metric<>(METRIC_PREFIX.concat("accounts"), 10L, ARBITRARY_DATE));
    }
}
