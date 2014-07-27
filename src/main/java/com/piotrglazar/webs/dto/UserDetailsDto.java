package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.Address;

import java.time.LocalDateTime;

public class UserDetailsDto {

    private final String username;
    private final String email;
    private final Address address;
    private final LocalDateTime memberSince;

    public UserDetailsDto(final String username, final String email, final Address address, final LocalDateTime memberSince) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.memberSince = memberSince;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }
}
