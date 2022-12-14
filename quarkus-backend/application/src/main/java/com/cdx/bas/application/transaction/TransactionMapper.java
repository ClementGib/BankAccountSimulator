package com.cdx.bas.application.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.bank.account.BankAccountRepository;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.MappingException;

@RequestScoped
public class TransactionMapper implements DtoEntityMapper<Transaction, TransactionEntity> {

    @Inject
    private BankAccountRepository bankAccountRepository;

    @Inject
    private ObjectMapper objectMapper;

    public Transaction toDto(TransactionEntity entity) {
        
        if (entity == null) {
            return null;
        }
        
        Transaction dto = new Transaction();

        dto.setId(entity.getId());
        
        if (entity.getAccount() != null) {
            dto.setAccountId(entity.getAccount().getId());
        }

        if (entity.getAmount() != null) {
            dto.setAmount(entity.getAmount().longValue());
        }
        
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setDate(entity.getDate());
        dto.setLabel(entity.getLabel());

        try {
            if (entity.getMetadatas() != null) {
                dto.setMetadatas(
                        objectMapper.readValue(entity.getMetadatas(), new TypeReference<Map<String, String>>() {}));
            } else {
                dto.setMetadatas(new HashMap<>());
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("An error occured while parsing JSON String to Map<String, String>", exception);
        }
        return dto;
    }

    public TransactionEntity toEntity(Transaction dto) {
        
        if (dto == null) {
            return null;
        }
        
        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.getId());
        Optional<BankAccountEntity> optionalBankAccountEntity = bankAccountRepository
                .findByIdOptional(dto.getAccountId());
        if (optionalBankAccountEntity.isPresent()) {
            entity.setAccount(optionalBankAccountEntity.get());
        } else {
            throw new NoSuchElementException("Bank Account entity not found for id: " + dto.getAccountId());
        }

        entity.setAmount(new BigDecimal(dto.getAmount()));
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setDate(dto.getDate());
        entity.setLabel(dto.getLabel());

        try {
            if (!dto.getMetadatas().isEmpty()) {
                entity.setMetadatas(objectMapper.writeValueAsString(dto.getMetadatas()));
            } else {
                entity.setMetadatas(null);
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("An error occured while parsing Map<String, String> to JSON String", exception);
        }
        return entity;
    }
}
