package com.piotrglazar.webs;

import java.util.List;

public interface ValidatorChain<A, B> {

    void validateAll(A object, B errorGatherer);

    List<? extends Validator<A, B>> validators();
}
