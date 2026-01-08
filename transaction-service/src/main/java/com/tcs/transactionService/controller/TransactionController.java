package com.tcs.transactionService.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.transactionService.dto.TransactionDto;
import com.tcs.transactionService.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/addMoney")
    public TransactionDto addMoney(@RequestBody TransactionDto dto) {
        return transactionService.addMoney(dto);
    }

    @PostMapping("/transfer")
    public TransactionDto transfer(@RequestBody TransactionDto dto) {
        return transactionService.transfer(dto);
    }
}

