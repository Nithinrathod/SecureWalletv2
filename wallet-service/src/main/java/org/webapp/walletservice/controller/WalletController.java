package org.webapp.walletservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webapp.walletservice.dto.WalletDto;
import org.webapp.walletservice.model.Wallet;
import org.webapp.walletservice.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    // Get Wallet Details
    @GetMapping("/{userId}")
    public WalletDto getDetails(@PathVariable String userId) {
        return walletService.toDto(userId);
    }

    // Create Wallet
    @PostMapping("/create")
    public WalletDto create(@RequestBody Wallet wallet) {
        return walletService.createWallet(wallet);
    }

    // Get Balance
    @GetMapping("/balance/{userId}")
    public WalletDto getBalance(@PathVariable String userId) {
        return walletService.getBalanceDto(userId);
    }

    // Block Wallet
    @PutMapping("/block/{userId}")
    public WalletDto block(@PathVariable String userId) {
        return walletService.updateStatus(userId, "BLOCKED");
    }

    // Unblock Wallet
    @PutMapping("/unblock/{userId}")
    public WalletDto unblock(@PathVariable String userId) {
        return walletService.updateStatus(userId, "ACTIVATED");
    }

    // Debit
    @PutMapping("/debit")
    public WalletDto debit(@RequestBody WalletDto req) {
        return walletService.debit(req.getUserId(), req.getAmount());
    }

    // Credit
    @PutMapping("/credit")
    public WalletDto credit(@RequestBody WalletDto req) {
        return walletService.credit(req.getUserId(), req.getAmount());
    }
    


}
