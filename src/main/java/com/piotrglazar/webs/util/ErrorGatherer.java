package com.piotrglazar.webs.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class ErrorGatherer {

    private List<String> errorMessages = Lists.newLinkedList();

    public void reportError(String errorMessage) {
        errorMessages.add(errorMessage);
    }

    public List<String> getErrorMessages() {
        return ImmutableList.copyOf(errorMessages);
    }

    public boolean isEmpty() {
        return errorMessages.isEmpty();
    }
}
