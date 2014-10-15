package com.piotrglazar.webs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.piotrglazar.webs.dto.MoneyTransferAuditUserDto.Kind;

public class PrintableMoneyTransferAudit {

    private final String message;

    public PrintableMoneyTransferAudit(Kind kind, Long userId, BigDecimal amount, Boolean success,
                                       LocalDateTime date, Long accountId) {
        final StringBuilder builder = buildMessage(kind, userId, amount, success, date, accountId);
        this.message = builder.toString();
    }

    private StringBuilder buildMessage(Kind kind, Long userId, BigDecimal amount, Boolean success,
                                       LocalDateTime date, Long accountId) {
        final StringBuilder builder = new StringBuilder();
        addTimeMessage(builder, date);
        addKindMessage(builder, kind);
        addAmountMessage(builder, amount);
        addUserMessage(builder, userId, kind);
        addAccountMessage(builder, accountId, kind);
        addSuccessMessage(builder, success);
        return builder;
    }

    private void addSuccessMessage(final StringBuilder builder, final Boolean success) {
        if (success) {
            builder.append(" - Success");
        } else {
            builder.append(" - Failure");
        }
    }

    private void addAccountMessage(final StringBuilder builder, final Long accountId, final Kind kind) {
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

    private void addUserMessage(final StringBuilder builder, final Long userId, final Kind kind) {
        kindRelatedMessages(builder, kind, " from ", " to ");
        builder
            .append("user ")
            .append(userId);
    }

    private void addAmountMessage(final StringBuilder builder, final BigDecimal amount) {
        builder
            .append(" ")
            .append(amount);
    }

    private void addKindMessage(final StringBuilder builder, final Kind kind) {
        builder.append(" you have ");
        kindRelatedMessages(builder, kind, "received", "sent");
    }

    private void addTimeMessage(final StringBuilder builder, final LocalDateTime date) {
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
                dto.getAccountId());
    }
}
