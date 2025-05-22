package com.eucl.tokensystem.controller;

import com.eucl.tokensystem.model.Meter;
import com.eucl.tokensystem.model.User;
import com.eucl.tokensystem.payload.request.MeterRequest;
import com.eucl.tokensystem.payload.response.MessageResponse;
import com.eucl.tokensystem.repository.MeterRepository;
import com.eucl.tokensystem.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/meters")
public class MeterController {
    @Autowired
    MeterRepository meterRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerMeter(@Valid @RequestBody MeterRequest meterRequest) {
        if (meterRepository.existsByMeterNumber(meterRequest.getMeterNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Meter number is already registered!"));
        }

        User user = userRepository.findByEmail(meterRequest.getUserEmail())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Meter meter = new Meter(meterRequest.getMeterNumber(), user);
        meterRepository.save(meter);

        return ResponseEntity.ok(new MessageResponse("Meter registered successfully!"));
    }

    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<?> getUserMeters(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        List<Meter> meters = meterRepository.findByOwner(user);
        return ResponseEntity.ok(meters);
    }

    @GetMapping("/{meterNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<?> getMeterByNumber(@PathVariable String meterNumber) {
        Meter meter = meterRepository.findByMeterNumber(meterNumber)
                .orElseThrow(() -> new RuntimeException("Error: Meter not found."));

        return ResponseEntity.ok(meter);
    }
}
