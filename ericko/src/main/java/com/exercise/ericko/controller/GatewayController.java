package com.exercise.ericko.controller;

import com.exercise.ericko.exception.ResourceNotFoundException;
import com.exercise.ericko.model.Gateway;
import com.exercise.ericko.repository.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class GatewayController {

    @Autowired
    private GatewayRepository gatewayRepository;

    @GetMapping("/gateways")
    public Page<Gateway> getAllGateways(Pageable pageable) {
        return gatewayRepository.findAll(pageable);
    }

    @GetMapping("/gateways/{gatewayId}")
    public Gateway getSingleGateway(@PathVariable Long gatewayId) {
        return gatewayRepository.findById(gatewayId).orElseThrow(() -> new ResourceNotFoundException("GatewayId " + gatewayId + " not found"));
    }

    @PostMapping("/gateways")
    public Gateway createGateway(@Valid @RequestBody Gateway gateway) {

            return gatewayRepository.save(gateway);

    }

    @PutMapping("/gateways/{gatewayId}")
    public Gateway updateGateway(@PathVariable Long gatewayId, @Valid @RequestBody Gateway gatewayRequest) {
        return gatewayRepository.findById(gatewayId).map(gateway -> {
            gateway.setGatewayName(gatewayRequest.getGatewayName());
            gateway.setIpv4(gatewayRequest.getIpv4());
            gateway.setSerialNumber(gatewayRequest.getSerialNumber());
            return gatewayRepository.save(gateway);
        }).orElseThrow(() -> new ResourceNotFoundException("GatewayId " + gatewayId + " not found"));
    }


    @DeleteMapping("/gateways/{gatewayId}")
    public ResponseEntity<?> deleteGateway(@PathVariable Long gatewayId) {
        return gatewayRepository.findById(gatewayId).map(gateway -> {
            gatewayRepository.delete(gateway);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("GatewayId " + gatewayId + " not found"));
    }

}