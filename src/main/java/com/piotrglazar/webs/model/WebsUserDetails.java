package com.piotrglazar.webs.model;


import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class WebsUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime memberSince;

    @Embedded
    private Address address;

    public WebsUserDetails(final LocalDateTime memberSince, final Address address) {
        this.memberSince = memberSince;
        this.address = address;
    }

    public WebsUserDetails() {

    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(final LocalDateTime memberSince) {
        this.memberSince = memberSince;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }
}
