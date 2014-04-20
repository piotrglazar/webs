package com.piotrglazar.webs.model;

import com.google.common.base.Objects;

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
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(indexes = @Index(unique = true, columnList = "username"))
public final class WebsUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "WEBSUSER_ID", referencedColumnName = "ID")
    private Set<Account> accounts;

    public WebsUser() {

    }

    public WebsUser(final String username, final String password, final Set<String> roles, final Set<Account> accounts) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accounts = accounts;
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

    public void setAccounts(final Set<Account> accounts) {
        this.accounts = accounts;
    }

    public static WebsUserBuilder builder() {
        return new WebsUserBuilder();
    }
}
