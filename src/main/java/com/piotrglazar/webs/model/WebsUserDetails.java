package com.piotrglazar.webs.model;


import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public final class WebsUserDetails {

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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WebsUserDetails other = (WebsUserDetails) obj;
        return Objects.equals(this.id, other.id);
    }
}
