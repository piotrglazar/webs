package com.piotrglazar.webs.model.entities;

import com.piotrglazar.webs.business.utils.Currency;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public final class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amountLoaned;

    private BigDecimal amountRemaining;

    private Currency currency;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<LocalDate> postpones;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    private Byte weeks;

    private Byte weeksRemaining;

    private transient boolean canPostpone;

    @Transient
    public Boolean getCanPostpone() {
        return canPostpone;
    }

    public void setCanPostpone(final Boolean canPostpone) {
        this.canPostpone = canPostpone;
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
        final Loan other = (Loan) obj;
        return Objects.equals(this.id, other.id);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmountLoaned() {
        return amountLoaned;
    }

    public void setAmountLoaned(final BigDecimal amountLoaned) {
        this.amountLoaned = amountLoaned;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(final BigDecimal amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public Set<LocalDate> getPostpones() {
        return postpones;
    }

    public void setPostpones(final Set<LocalDate> postpones) {
        this.postpones = postpones;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public Byte getWeeks() {
        return weeks;
    }

    public void setWeeks(final Byte weeks) {
        this.weeks = weeks;
    }

    public Byte getWeeksRemaining() {
        return weeksRemaining;
    }

    public void setWeeksRemaining(final Byte weeksRemaining) {
        this.weeksRemaining = weeksRemaining;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void subtractAmountRemaining(final BigDecimal payment) {
        amountRemaining = amountRemaining.subtract(payment);
    }

    public void payOneWeek(final BigDecimal payment) {
        if (weeksRemaining > 0) {
            --weeksRemaining;
        }
        subtractAmountRemaining(payment);
    }
}
