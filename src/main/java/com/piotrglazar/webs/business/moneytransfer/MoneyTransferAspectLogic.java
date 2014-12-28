package com.piotrglazar.webs.business.moneytransfer;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.model.entities.WebsUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

@Component
public class MoneyTransferAspectLogic {

    private final Supplier<LocalDateTime> dateSupplier;
    private final MoneyTransferAuditProvider auditProvider;
    private final UserProvider userProvider;

    @Autowired
    public MoneyTransferAspectLogic(Supplier<LocalDateTime> dateSupplier, MoneyTransferAuditProvider auditProvider,
                               UserProvider userProvider) {
        this.dateSupplier = dateSupplier;
        this.auditProvider = auditProvider;
        this.userProvider = userProvider;
    }

    @SuppressWarnings("all")
    public Object aroundMoneyTransfer(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final MoneyTransferParams moneyTransferParams = extractMoneyTransferParams(proceedingJoinPoint.getArgs());
        try {
            final Object result = proceedingJoinPoint.proceed();
            recordSuccessfulMoneyTransfer(moneyTransferParams);
            return result;
        } catch (Throwable t) {
            recordUnsuccessfulMoneyTransfer(moneyTransferParams);
            throw t;
        }
    }

    private void recordUnsuccessfulMoneyTransfer(final MoneyTransferParams moneyTransferParams) {
        recordMoneyTransfer(moneyTransferParams, false);
    }

    private void recordSuccessfulMoneyTransfer(final MoneyTransferParams moneyTransferParams) {
        recordMoneyTransfer(moneyTransferParams, true);
    }

    private void recordMoneyTransfer(final MoneyTransferParams moneyTransferParams, final boolean result) {
        Long userId = failSafeUserId(moneyTransferParams);

        auditProvider.auditMoneyTransfer(userId, moneyTransferParams.getFromAccount(), moneyTransferParams.getToAccount(),
                moneyTransferParams.getAmount(), result, dateSupplier.get(), moneyTransferParams.getReceivingUserId(),
                moneyTransferParams.getCurrency());
    }

    private Long failSafeUserId(final MoneyTransferParams moneyTransferParams) {
        WebsUser user = userProvider.findUserByUsername(moneyTransferParams.getUsername());

        if (user != null) {
            return user.getId();
        } else {
            return -1L;
        }
    }

    private MoneyTransferParams extractMoneyTransferParams(final Object[] args) {
        return (MoneyTransferParams) Arrays.stream(args)
                .filter(arg -> arg instanceof MoneyTransferParams)
                .findFirst()
                .orElseThrow(() -> moneyTransferParamsNotFound(args));
    }

    private IllegalStateException moneyTransferParamsNotFound(Object[] args) {
        return new IllegalStateException("Could not find MoneyTransferParams in ".concat(extractTypes(args)));
    }

    private String extractTypes(final Object[] args) {
        return asList(args).stream()
                .map(obj -> obj.getClass().getSimpleName())
                .collect(joining(","));
    }
}
