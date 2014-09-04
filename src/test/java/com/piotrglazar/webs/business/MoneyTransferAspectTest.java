package com.piotrglazar.webs.business;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class MoneyTransferAspectTest {

    @Mock
    private Supplier<LocalDateTime> localDateTimeSupplier;

    @Mock
    private MoneyTransferAuditProvider auditProvider;

    @Mock
    private UserProvider userProvider;

    @InjectMocks
    private MoneyTransferAspect aspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Before
    public void setUp() {
        given(localDateTimeSupplier.get()).willReturn(LocalDateTime.of(2014, 5, 26, 23, 38));
    }

    @Test
    public void shouldThrowExceptionWhenNoMoneyTransferParamsProvided() throws Throwable {
        // given
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{1L, "abc", new Object()});

        // when
        catchException(aspect).aroundMoneyTransfer(proceedingJoinPoint);

        // then
        assertThat((Exception) caughtException()).isInstanceOf(RuntimeException.class).hasMessageContaining("Long,String,Object");
    }

    @Test
    public void shouldUseMinusOneForNonExistingUserId() throws Throwable {
        // given
        MoneyTransferParams params = moneyTransferParams();
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{params});

        // when
        aspect.aroundMoneyTransfer(proceedingJoinPoint);

        // then
        verify(auditProvider).auditMoneyTransfer(-1L, params.getFromAccount(), params.getToAccount(), params.getAmount(), true,
                localDateTimeSupplier.get(), params.getReceivingUserId());
    }

    @Test
    public void shouldStoreSuccessfulTransfer() throws Throwable {
        // given
        MoneyTransferParams params = moneyTransferParams();
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{params});
        given(userProvider.findUserByUsername("user")).willReturn(WebsUser.builder().id(555L).build());

        // when
        aspect.aroundMoneyTransfer(proceedingJoinPoint);

        // then
        verify(auditProvider).auditMoneyTransfer(555L, params.getFromAccount(), params.getToAccount(), params.getAmount(), true,
                localDateTimeSupplier.get(), params.getReceivingUserId());
    }

    @Test
    public void shouldStoreFailedTransferAttempt() throws Throwable {
        // given
        MoneyTransferParams params = moneyTransferParams();
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{params});
        given(userProvider.findUserByUsername("user")).willReturn(WebsUser.builder().id(555L).build());
        given(proceedingJoinPoint.proceed()).willThrow(RuntimeException.class);

        // when
        catchException(aspect).aroundMoneyTransfer(proceedingJoinPoint);

        // then
        verify(auditProvider).auditMoneyTransfer(555L, params.getFromAccount(), params.getToAccount(), params.getAmount(), false,
                localDateTimeSupplier.get(), params.getReceivingUserId());
    }

    private MoneyTransferParams moneyTransferParams() {
        return new MoneyTransferParams("user", "u@u.pl", 123L, 321L, new BigDecimal("15"), 1L, "user2");
    }
}
