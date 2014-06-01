package com.piotrglazar.webs.business;

import com.google.common.collect.ImmutableList;
import com.piotrglazar.webs.Validator;
import com.piotrglazar.webs.ValidatorChain;
import com.piotrglazar.webs.util.ErrorGatherer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MoneyTransferValidatorChain implements ValidatorChain<MoneyTransferDetails, ErrorGatherer> {

    private final List<MoneyTransferValidator> validators;

    @Autowired
    public MoneyTransferValidatorChain(final List<MoneyTransferValidator> validators) {
        this.validators = ImmutableList.copyOf(validators);
    }

    @Override
    public void validateAll(final MoneyTransferDetails object, final ErrorGatherer errorGatherer) {
        for (final MoneyTransferValidator validator : validators) {
            validator.validate(object, errorGatherer);
        }
    }

    @Override
    public List<? extends Validator<MoneyTransferDetails, ErrorGatherer>> validators() {
        return validators;
    }
}
