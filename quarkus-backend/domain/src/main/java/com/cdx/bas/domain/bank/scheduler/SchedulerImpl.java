package com.cdx.bas.domain.bank.scheduler;

import java.util.PriorityQueue;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionManager;
import com.cdx.bas.domain.bank.transaction.TransactionService;

import org.jboss.logging.Logger;

import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;

@Startup
@Singleton
@Transactional(value = TxType.NEVER)
public class SchedulerImpl implements Scheduler {

    private static final Logger logger = Logger.getLogger(SchedulerImpl.class);
    
    private static PriorityQueue<Transaction> currentQueue = new PriorityQueue<Transaction>();

    @Inject
    TransactionService transactionService;
    
    @Inject
    TransactionManager transactionManager;

    @Scheduled(every="5s")
    public void processQueue() {

    }
}