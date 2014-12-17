package com.piotrglazar.webs.business.exceptions;

import com.piotrglazar.webs.WebsRuntimeException;

public class NoSuchNewsImporterException extends WebsRuntimeException {

    public NoSuchNewsImporterException(final int newsImporterIndex, final int numberOfNewsImporters) {
        super(String.format("Illegal news importer index %s, current size is %s", newsImporterIndex, numberOfNewsImporters));
    }
}
