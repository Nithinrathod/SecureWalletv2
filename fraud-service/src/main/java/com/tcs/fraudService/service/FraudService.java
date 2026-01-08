package com.tcs.fraudService.service;

import org.springframework.stereotype.Service;

import com.tcs.fraudService.bean.FraudRecord;
import com.tcs.fraudService.dto.FraudDto;
import com.tcs.fraudService.repository.FraudRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final FraudRepository fraudRepository;

    // Simple blacklist just for demo
    private static final String BLACKLIST_USER = "FRAUD_USER";

    public FraudDto check(FraudDto req) {

        FraudDto response = new FraudDto();
        response.setWalletId(req.getWalletId());
        response.setAmount(req.getAmount());

        String decision;
        String reason;

        if (BLACKLIST_USER.equals(req.getWalletId())) {
            decision = "BLOCK";
            reason = "User blacklisted";
        }
        else if (req.getAmount() != null && req.getAmount() > 10000) {
            decision = "BLOCK";
            reason = "High transaction amount";
        }
        else {
            decision = "ALLOW";
            reason = "No fraud detected";
        }

        response.setDecision(decision);
        response.setReason(reason);
        response.setMessage("Fraud check completed");

        // Save in DB
        FraudRecord record = new FraudRecord();
        record.setWalletId(req.getWalletId());
        record.setAmount(req.getAmount());
        record.setDecision(decision);
        record.setReason(reason);

        fraudRepository.save(record);

        return response;
    }
}
