package com.exercise.ericko.controller;


import com.exercise.ericko.exception.ResourceNotFoundException;
import com.exercise.ericko.model.PeripheralDevice;
import com.exercise.ericko.repository.GatewayRepository;
import com.exercise.ericko.repository.PeripheralDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PeripheralDeviceController {

    @Autowired
    private PeripheralDeviceRepository devicesRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @GetMapping("/gateways/{gatewayId}/devices")
    public Page<PeripheralDevice> getAllDevicesByGatewayId(@PathVariable (value = "gatewayId") Long gatewayId,
                                                         Pageable pageable) {
        return devicesRepository.findByGatewayId(gatewayId, pageable);
    }

    @PostMapping("/gateways/{gatewayId}/devices")
    public PeripheralDevice createPeripheralDevice(@PathVariable (value = "gatewayId") Long gatewayId,
                                 @Valid @RequestBody PeripheralDevice device) {
        return gatewayRepository.findById(gatewayId).map(gateway -> {

            if(gateway.getDevices().size() < 10){
                device.setGateway(gateway);
                return devicesRepository.save(device);
            }
            else {
                throw new ResourceNotFoundException("Gateway id " + gatewayId + " cannot have more than 10 devices!");
            }

        }).orElseThrow(() -> new ResourceNotFoundException("gatewayId " + gatewayId + " not found"));
    }

    @PutMapping("/gateways/{gatewayId}/devices/{deviceId}")
    public PeripheralDevice updatePeripheralDevice(@PathVariable (value = "gatewayId") Long gatewayId,
                                 @PathVariable (value = "deviceId") Long deviceId,
                                 @Valid @RequestBody PeripheralDevice deviceRequest) {
        if(!gatewayRepository.existsById(gatewayId)) {
            throw new ResourceNotFoundException("gatewayId " + gatewayId + " not found");
        }

        return devicesRepository.findById(deviceId).map(device -> {
            device.setStatus(deviceRequest.getStatus());
            device.setVendorName(deviceRequest.getVendorName());
            return devicesRepository.save(device);
        }).orElseThrow(() -> new ResourceNotFoundException("deviceId " + deviceId + "not found"));
    }

    @DeleteMapping("/gateways/{gatewayId}/devices/{deviceId}")
    public ResponseEntity<?> deletePeripheralDevice(@PathVariable (value = "gatewayId") Long gatewayId,
                                           @PathVariable (value = "deviceId") Long deviceId) {
        return devicesRepository.findByIdAndGatewayId(deviceId, gatewayId).map(device -> {
            devicesRepository.delete(device);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId + " and gatewayId " + gatewayId));
    }
}