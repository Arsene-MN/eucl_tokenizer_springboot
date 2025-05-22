package com.eucl.tokensystem.repository;

import com.eucl.tokensystem.model.Meter;
import com.eucl.tokensystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {
    Optional<Meter> findByMeterNumber(String meterNumber);
    
    Boolean existsByMeterNumber(String meterNumber);
    
    List<Meter> findByOwner(User owner);
}
