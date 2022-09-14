package com.cdx.bas.domain.bank.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionManager;
import com.cdx.bas.domain.bank.transaction.TransactionService;
import com.cdx.bas.domain.bank.transaction.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class SchedulerImplTest {

    @Inject
    Scheduler scheduler;

    @InjectMock
    TransactionService transactionService;

    @InjectMock
    TransactionManager transactionManager;

//        @Test
//        public void processQueue_should_getUnprocessedTransactions_when_queueIsEmpty() {
//            transactionService.pr
//        }
//        
//        @Test
//        public void processQueue_should_getUnprocessedTransactionsAndProcressThem_when_queueHasBeenFilled() {
//            
//        }

    @Test
//        @Order(1)
    public void processQueue_should_fillTheQueueAndProcessOrderedTransactions_when_QueueWasEmptyAndWillBeFilledWithOrderedTransactions() {
        Queue<Transaction> queue = createDepositTransactions();
        when(transactionManager.getUnprocessedTransactions()).thenReturn(queue);
        Clock clock;

        scheduler.processQueue();

        verify(transactionManager).getUnprocessedTransactions();
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo(new Transaction(16L, 20L, TransactionType.CREDIT,
                TransactionStatus.WAITING, Instant.MIN, "Fifth transaction"));
        verify(transactionService).processTransaction(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-06T10:14:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo(new Transaction(50L, 120L, TransactionType.CREDIT,
                TransactionStatus.WAITING, Instant.now(clock), "Third transaction"));
        verify(transactionService).processTransaction(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-07T10:14:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo(new Transaction(100L, 99L, TransactionType.CREDIT,
                TransactionStatus.WAITING, Instant.now(clock), "Second transaction"));
        verify(transactionService).processTransaction(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-07T10:18:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo(new Transaction(99L, 1000L,
                TransactionType.CREDIT, TransactionStatus.WAITING, Instant.now(clock), "Fourth transaction"));
        verify(transactionService).processTransaction(queue.poll());

        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo(new Transaction(99L, 250L, TransactionType.CREDIT,
                TransactionStatus.WAITING, Instant.MAX, "First transaction"));
        verify(transactionService).processTransaction(queue.poll());
        verifyNoMoreInteractions(transactionService);
    }

    @Test
//        @Order(2)
    public void processQueue_should_DoNothing_when_SchedulerIsAlreadyInUse() throws InterruptedException {
        scheduler.processQueue();
        verifyNoInteractions(transactionService);
    }

    @Test
//        @Order(3)
    public void processQueue_should_runScheduleProcessQueues_when_WaitingForScheduler() throws InterruptedException {
        Queue<Transaction> queue = createDepositTransactions();
        when(transactionManager.getUnprocessedTransactions()).thenReturn(queue);

        Thread.sleep(5000);

        verify(transactionManager).getUnprocessedTransactions();
        verify(transactionService, times(5)).processTransaction(any());
        verifyNoMoreInteractions(transactionService);
    }

    @RequestScoped
    public static class TransactionManagerImplMock implements TransactionManager {

        @Override
        public Queue<Transaction> getUnprocessedTransactions() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    Queue<Transaction> createDepositTransactions() {
        Clock clock;
        Queue<Transaction> queue = new PriorityQueue<Transaction>();
        queue.add(new Transaction(99L, 250L, TransactionType.CREDIT, TransactionStatus.WAITING, Instant.MAX,
                "First transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-07T10:14:00Z"), ZoneId.of("UTC"));
        queue.add(new Transaction(100L, 99L, TransactionType.CREDIT, TransactionStatus.WAITING, Instant.now(clock),
                "Second transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-06T10:14:00Z"), ZoneId.of("UTC"));
        queue.add(new Transaction(50L, 120L, TransactionType.CREDIT, TransactionStatus.WAITING, Instant.now(clock),
                "Third transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-07T10:18:00Z"), ZoneId.of("UTC"));
        queue.add(new Transaction(99L, 1000L, TransactionType.CREDIT, TransactionStatus.WAITING, Instant.now(clock),
                "Fourth transaction"));
        queue.add(new Transaction(16L, 20L, TransactionType.CREDIT, TransactionStatus.WAITING, Instant.MIN,
                "Fifth transaction"));
        return queue;
    }

}
