package org.webapp.walletservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webapp.walletservice.dto.WalletDto;
import org.webapp.walletservice.model.Wallet;
import org.webapp.walletservice.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    // Create Wallet
    public WalletDto createWallet(Wallet wallet) {

        if (walletRepository.findByUserId(wallet.getUserId()).isPresent()) {
            throw new RuntimeException("Wallet already exists for User ID: " + wallet.getUserId());
        }

        Wallet saved = walletRepository.save(wallet);

        WalletDto dto = new WalletDto();
        dto.setWalletId(saved.getWalletId());
        dto.setUserId(saved.getUserId());
        dto.setBalance(saved.getBalance());
        dto.setStatus(saved.getStatus());
        dto.setMessage("Wallet Created Successfully");
        return dto;
    }

    // Get Wallet By User Id
    public Wallet getWallet(String userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for User: " + userId));
    }

    // Update Wallet Status
    public WalletDto updateStatus(String userId, String newStatus) {
        Wallet wallet = getWallet(userId);

        if(wallet.getStatus().equals(newStatus)) {
            WalletDto dto = toDto(wallet);
            dto.setMessage("Wallet already in " + newStatus + " state");
            return dto;
        }

        wallet.setStatus(newStatus);
        walletRepository.save(wallet);

        WalletDto dto = toDto(wallet);
        dto.setMessage("Wallet has been " + newStatus + " successfully");
        return dto;
    }

    // Debit
    public WalletDto debit(String userId, double amount) {
        Wallet wallet = getWallet(userId);

        if ("BLOCKED".equals(wallet.getStatus())) {
            throw new RuntimeException("Transaction failed: Wallet is blocked.");
        }

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Transaction failed: Insufficient balance.");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        WalletDto dto = toDto(wallet);
        dto.setAmount(amount);
        dto.setMessage("Amount debited successfully");
        return dto;
    }

    // Credit
    public WalletDto credit(String userId, double amount) {
        Wallet wallet = getWallet(userId);

        if ("BLOCKED".equals(wallet.getStatus())) {
            throw new RuntimeException("Transaction failed: Wallet is blocked.");
        }

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        WalletDto dto = toDto(wallet);
        dto.setAmount(amount);
        dto.setMessage("Amount credited successfully");
        return dto;
    }

    // ---------- Utility Mapper ----------
    private WalletDto toDto(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setWalletId(wallet.getWalletId());
        dto.setUserId(wallet.getUserId());
        dto.setBalance(wallet.getBalance());
        dto.setStatus(wallet.getStatus());
        return dto;
    }
    
 // Convert userId -> WalletDto
    public WalletDto toDto(String userId) {
        Wallet wallet = getWallet(userId);
        WalletDto dto = toDto(wallet);
        dto.setMessage("Wallet fetched successfully");
        return dto;
    }

    // Get only balance but still return WalletDto
    public WalletDto getBalanceDto(String userId) {
        Wallet wallet = getWallet(userId);
        WalletDto dto = toDto(wallet);
        dto.setMessage("Balance fetched successfully");
        return dto;
    }

}
