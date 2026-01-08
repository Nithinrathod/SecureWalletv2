package org.webapp.walletservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webapp.walletservice.model.Wallet;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByUserId(String userId);
}
