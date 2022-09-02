package com.cdx.bas.domain.bank.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Test
    public void validate_should_validateTransactionObject_when_fillAllFieldsWithValidValues() {
        Instant date = Instant.now();
        Transaction transaction = new Transaction(99L, 1000L, TransactionType.CREDIT, date, "Deposit of 100 euros");
        transaction.validate();

        assertThat(transaction.accountId()).isEqualTo(99L);
        assertThat(transaction.amount()).isEqualTo(1000L);
        assertThat(transaction.type()).isEqualTo(TransactionType.CREDIT);
        assertThat(transaction.date()).isEqualTo(date);
        assertThat(transaction.label()).hasToString("Deposit of 100 euros");
    }

    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_creditTransactionWithNegativeAmount() {
        try {
            Instant date = Instant.now();
            Transaction transaction = new Transaction(99L, -100L, TransactionType.CREDIT, date, "Deposit of 100 euros");
            transaction.validate();

            fail();
        } catch (IllegalStateException exception) {
            String[] errorMessages = exception.getMessage().split("\n");
            assertThat(errorMessages).hasSize(1);
            assertThat(errorMessages[0]).hasToString("Credit transaction amount must be greater than 0.");
        }

    }

    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_debitTransactionWithNegativeAmount() {
        try {
            Instant date = Instant.now();
            Transaction transaction = new Transaction(99L, -100L, TransactionType.DEBIT, date, "Deposit of 100 euros");
            transaction.validate();

            fail();
        } catch (IllegalStateException exception) {
            String[] errorMessages = exception.getMessage().split("\n");
            assertThat(errorMessages).hasSize(1);
            assertThat(errorMessages[0]).hasToString("Debit transaction amount must be greater than 0.");
        }
    }

    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_creditTransactionWithZeroAmount() {
        try {
            Instant date = Instant.now();
            Transaction transaction = new Transaction(99L, 0L, TransactionType.CREDIT, date, "Deposit of 100 euros");
            transaction.validate();

            fail();
        } catch (IllegalStateException exception) {
            String[] errorMessages = exception.getMessage().split("\n");
            assertThat(errorMessages).hasSize(1);
            assertThat(errorMessages[0]).hasToString("Credit transaction amount must be greater than 0.");
        }
    }

    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_debitTransactionWitZeroAmount() {
        try {
            Instant date = Instant.now();
            Transaction transaction = new Transaction(99L, 0L, TransactionType.DEBIT, date, "Deposit of 100 euros");
            transaction.validate();

            fail();
        } catch (IllegalStateException exception) {
            String[] errorMessages = exception.getMessage().split("\n");
            assertThat(errorMessages).hasSize(1);
            assertThat(errorMessages[0]).hasToString("Debit transaction amount must be greater than 0.");
        }
    }

    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_fieldsAreNullOrInvalid() {
        try {
            Transaction transaction = new Transaction(0, 0, null, null, null);
            transaction.validate();

            fail();
        } catch (IllegalStateException exception) {
            String[] errorMessages = exception.getMessage().split("\n");
            assertThat(errorMessages).hasSize(3);
            assertThat(errorMessages[0]).hasToString("Transaction type must not be null.");
            assertThat(errorMessages[1]).hasToString("Transaction date must not be null.");
            assertThat(errorMessages[2]).hasToString("Transaction label must not be null.");
        }
    }
}