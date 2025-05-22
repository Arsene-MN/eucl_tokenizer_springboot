package com.eucl.tokensystem.repository;

import com.eucl.tokensystem.model.Meter;
import com.eucl.tokensystem.model.PurchasedToken;
import com.eucl.tokensystem.model.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<PurchasedToken, Long> {
    List<PurchasedToken> findByMeterOrderByPurchasedDateDesc(Meter meter);
    
    List<PurchasedToken> findByMeterNumberOrderByPurchasedDateDesc(String meterNumber);
    
    Optional<PurchasedToken> findByToken(String token);
    
    @Query("SELECT t FROM PurchasedToken t WHERE t.tokenStatus = :status AND t.expiryDate BETWEEN :startTime AND :endTime")
    List<PurchasedToken> findTokensAboutToExpire(TokenStatus status, LocalDateTime startTime, LocalDateTime endTime);
}
