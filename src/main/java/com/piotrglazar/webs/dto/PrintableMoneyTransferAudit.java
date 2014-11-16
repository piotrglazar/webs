package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.piotrglazar.webs.dto.MoneyTransferAuditUserDto.Kind;

public class PrintableMoneyTransferAudit {

    private final String message;

    public PrintableMoneyTransferAudit(Kind kind, Long userId, BigDecimal amount, Boolean success,
                                       LocalDateTime date, Long accountId, Currency currency) {
        final StringBuilder builder = buildMessage(kind, userId, amount, success, date, accountId, currency);
        this.message = builder.toString();
    }

    private StringBuilder buildMessage(Kind kind, Long userId, BigDecimal amount, Boolean success,
                                       LocalDateTime date, Long accountId, Currency currency) {
        final StringBuilder builder = new StringBuilder();
        addTimeMessage(builder, date);
        addKindMessage(builder, kind);
        addAmountMessage(builder, amount, currency);
        addUserMessage(builder, userId, kind);
        addAccountMessage(builder, accountId, kind);
        addSuccessMessage(builder, success);
        return builder;
    }

    private void addSuccessMessage(StringBuilder builder, Boolean success) {
        if (success) {
            builder.append(" - Success");
        } else {
            builder.append(" - Failure");
        }
    }

    private void addAccountMessage(StringBuilder builder, Long accountId, Kind kind) {
        kindRelatedMessages(builder, kind, " to ", " from ");
        builder
            .append("account ")
            .append(accountId);
    }

    private void kindRelatedMessages(StringBuilder builder, Kind kind, String incomingKindMessage, String outgoingKindMessage) {
        if (kind == Kind.INCOMING) {
            builder.append(incomingKindMessage);
        } else if (kind == Kind.OUTGOING) {
            builder.append(outgoingKindMessage);
        } else {
            throw new IllegalStateException("Unknown kind " + kind);
        }
    }

    private void addUserMessage(StringBuilder builder, Long userId, Kind kind) {
        kindRelatedMessages(builder, kind, " from ", " to ");
        builder
            .append("user ")
            .append(userId);
    }

    private void addAmountMessage(StringBuilder builder, BigDecimal amount, Currency currency) {
        builder
            .append(" ")
            .append(amount)
            .append(" ")
            .append(currency);
    }

    private void addKindMessage(StringBuilder builder, Kind kind) {
        builder.append(" you have ");
        kindRelatedMessages(builder, kind, "received", "sent");
    }

    private void addTimeMessage(StringBuilder builder, LocalDateTime date) {
        builder
            .append("On ")
            .append(date);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public static PrintableMoneyTransferAudit from(MoneyTransferAuditUserDto dto) {
        return new PrintableMoneyTransferAudit(dto.getKind(), dto.getUserId(), dto.getAmount(), dto.getSuccess(), dto.getDate(),
                dto.getAccountId(), dto.getCurrency());
    }
}
