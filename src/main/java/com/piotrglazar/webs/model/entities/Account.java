package com.piotrglazar.webs.model.entities;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Inheritance
@DiscriminatorColumn(name = "ACCOUNT_TYPE")
@Table(indexes = {
        @Index(unique = true, columnList = "number")
})
public abstract class Account {

    public static final int BALANCE_PRECISION = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(scale = 2, precision = BALANCE_PRECISION, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Subaccount> subaccounts = Lists.newArrayList();

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

    @PotentiallyExpensiveOperation
    public BigDecimal getTotalBalance() {
        return Stream.concat(Stream.of(balance), subaccounts.stream().map(Subaccount::getBalance)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Subaccount> getSubaccounts() {
        return subaccounts;
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

    public Account subtractBalance(final BigDecimal amount) {
        this.balance = balance.subtract(amount);
        return this;
    }

    public abstract AccountType accountType();

    public void addSubaccount(final Subaccount newSubaccount) {
        subaccounts.add(newSubaccount);
    }
}
