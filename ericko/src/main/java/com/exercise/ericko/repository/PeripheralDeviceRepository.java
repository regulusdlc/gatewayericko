package com.exercise.ericko.repository;

import com.exercise.ericko.model.PeripheralDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeripheralDeviceRepository extends JpaRepository<PeripheralDevice, Long> {
    Page<PeripheralDevice> findByGatewayId(Long gatewayId, Pageable pageable);
    Optional<PeripheralDevice> findByIdAndGatewayId(Long id, Long gatewayId);
}