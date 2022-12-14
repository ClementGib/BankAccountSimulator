package com.cdx.bas.application.bank.transaction;

import static com.cdx.bas.domain.transaction.TransactionStatus.WAITING;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class TransactionServiceImplTest {

	@Inject
	TransactionServicePort transactionService;

	@InjectMock
	BankAccountServicePort bankAccountService;

	@Test
	public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithPositiveAmount() {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(100L);
		transaction.setAccountId(100L);
		transaction.setType(CREDIT);
		transaction.setStatus(WAITING);
		transaction.setDate(Instant.now());
		transaction.setLabel("deposit of 100 euros");
		transactionService.processTransaction(transaction);

		verify(bankAccountService).deposit(transaction);
		verifyNoMoreInteractions(bankAccountService);
	}

	@Test
	public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithNegativeAmount() {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(-100L);
		transaction.setAccountId(991L);
		transaction.setType(CREDIT);
		transaction.setStatus(WAITING);
		transaction.setDate(Instant.now());
		transaction.setLabel("deposit of -100 euros");
		transactionService.processTransaction(transaction);

		verify(bankAccountService).deposit(transaction);
		verifyNoMoreInteractions(bankAccountService);
	}
}
