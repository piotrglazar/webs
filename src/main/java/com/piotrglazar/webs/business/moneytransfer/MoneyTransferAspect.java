package com.piotrglazar.webs.business.moneytransfer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MoneyTransferAspect {

    private final MoneyTransferAspectLogic moneyTransferAspectLogic;

    @Autowired
    public MoneyTransferAspect(MoneyTransferAspectLogic moneyTransferAspectLogic) {
        this.moneyTransferAspectLogic = moneyTransferAspectLogic;
    }

    @SuppressWarnings("all")
    @Around("execution(* com.piotrglazar.webs.business.moneytransfer.AccountMoneyTransfer.transferMoney(..))")
    public Object aroundMoneyTransfer(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return moneyTransferAspectLogic.aroundMoneyTransfer(proceedingJoinPoint);
    }
}
