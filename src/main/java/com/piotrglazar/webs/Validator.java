package com.piotrglazar.webs;

public interface Validator<A, B> {

    void validate(A object, B errorGatherer);
}
