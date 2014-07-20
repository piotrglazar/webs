package com.piotrglazar.webs.model;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Inheritance
@DiscriminatorColumn(name = "ACCOUNT_TYPE")
@Table(indexes = {
        @Index(unique = true, columnList = "number")
})
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected String number;

    @Enumerated(EnumType.STRING)
    protected Currency currency;

    @Column(scale = 2, precision = 10)
    protected BigDecimal balance;

    protected Account() {
    }

    protected Account(final String number, final Currency currency, final BigDecimal balance) {
        this.number = number;
        this.currency = currency;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }
    
    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        return Objects.equal(this.id, other.id);
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Account addBalance(final BigDecimal amount) {
        this.balance = balance.add(amount);
        return this;
    }

    public void subtractBalance(final BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public abstract AccountType accountType();
}
