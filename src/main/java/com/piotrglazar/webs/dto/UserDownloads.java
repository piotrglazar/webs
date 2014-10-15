package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public final class UserDownloads {

    private final String filename;
    private final List<String> content;

    public UserDownloads(final String filename, final List<String> content) {
        this.filename = filename;
        this.content = ImmutableList.copyOf(content);
    }

    public String getFilename() {
        return filename;
    }

    public List<String> getContent() {
        return content;
    }
}
