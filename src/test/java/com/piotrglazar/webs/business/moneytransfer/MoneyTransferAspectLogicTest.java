package com.piotrglazar.webs.business.moneytransfer;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.WebsUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class MoneyTransferAspectLogicTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Mock
    private Supplier<LocalDateTime> localDateTimeSupplier;

    @Mock
    private MoneyTransferAuditProvider auditProvider;

    @Mock
    private UserProvider userProvider;

    @InjectMocks
    private MoneyTransferAspectLogic aspectLogic;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Before
    public void setUp() {
        given(localDateTimeSupplier.get()).willReturn(LocalDateTime.of(2014, 5, 26, 23, 38));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenNoMoneyTransferParamsProvided() throws Throwable {
        // given
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{1L, "abc", new Object()});

        // when
        aspectLogic.aroundMoneyTransfer(proceedingJoinPoint);
    }

    @Test
    public void shouldUseMinusOneForNonExistingUserId() throws Throwable {
        // given
        MoneyTransferParams params = moneyTransferParams();
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{params});

        // when
        aspectLogic.aroundMoneyTransfer(proceedingJoinPoint);

        // then
        verify(auditProvider).auditMoneyTransfer(-1L, params.getFromAccount(), params.getToAccount(), params.getAmount(), true,
                localDateTimeSupplier.get(), params.getReceivingUserId(), Currency.GBP);
    }

    @Test
    public void shouldStoreSuccessfulTransfer() throws Throwable {
        // given
        MoneyTransferParams params = moneyTransferParams();
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{params});
        given(userProvider.findUserByUsername("user")).willReturn(WebsUser.builder().id(555L).build());

        // when
        aspectLogic.aroundMoneyTransfer(proceedingJoinPoint);

        // then
        verify(auditProvider).auditMoneyTransfer(555L, params.getFromAccount(), params.getToAccount(), params.getAmount(), true,
                localDateTimeSupplier.get(), params.getReceivingUserId(), Currency.GBP);
    }

    @Test
    public void shouldStoreFailedTransferAttempt() throws Throwable {
        // given
        MoneyTransferParams params = moneyTransferParams();
        given(proceedingJoinPoint.getArgs()).willReturn(new Object[]{params});
        given(userProvider.findUserByUsername("user")).willReturn(WebsUser.builder().id(555L).build());
        given(proceedingJoinPoint.proceed()).willThrow(RuntimeException.class);

        // when
        aroundMoneyTransferWrapper(aspectLogic, proceedingJoinPoint);

        // then
        verify(auditProvider).auditMoneyTransfer(555L, params.getFromAccount(), params.getToAccount(), params.getAmount(), false,
                localDateTimeSupplier.get(), params.getReceivingUserId(), Currency.GBP);
    }

    private void aroundMoneyTransferWrapper(MoneyTransferAspectLogic aspect, ProceedingJoinPoint proceedingJoinPoint) {
        try {
            aspect.aroundMoneyTransfer(proceedingJoinPoint);
        } catch (Throwable throwable) {
            LOG.info("Expected exception", throwable);
        }
    }

    private MoneyTransferParams moneyTransferParams() {
        return new MoneyTransferParams("user", "u@u.pl", 123L, 321L, new BigDecimal("15"), 1L, "user2", Currency.GBP);
    }

}
