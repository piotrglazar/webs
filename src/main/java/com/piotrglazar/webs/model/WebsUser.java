package com.piotrglazar.webs.model;

import com.google.common.base.Objects;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(unique = true, columnList = "username"),
        @Index(unique = true, columnList = "email")
})
public final class WebsUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "WEBSUSER_ID", referencedColumnName = "ID")
    private Set<Account> accounts;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "WEBSUSER_ID", referencedColumnName = "ID")
    private WebsUserDetails details;

    public WebsUser() {

    }

    public WebsUser(String username, String password, Set<String> roles, Set<Account> accounts, String email) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accounts = accounts;
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WebsUser other = (WebsUser) obj;
        return Objects.equal(this.id, other.id);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(final Set<String> roles) {
        this.roles = roles;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setAccounts(final Set<Account> accounts) {
        this.accounts = accounts;
    }

    public static WebsUserBuilder builder() {
        return new WebsUserBuilder();
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public WebsUserDetails getDetails() {
        return details;
    }

    public void setDetails(final WebsUserDetails details) {
        this.details = details;
    }
}
