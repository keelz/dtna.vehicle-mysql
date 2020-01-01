package com.dtna.vehicle.controller;

import com.dtna.vehicle.domain.Vehicle;
import com.dtna.vehicle.domain.VehicleBuilder;
import com.dtna.vehicle.repository.CommonRepository;
import com.dtna.vehicle.validation.VehicleValidationError;
import com.dtna.vehicle.validation.VehicleValidationErrorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private CommonRepository<Vehicle> repository;

    public VehicleController(CommonRepository<Vehicle> repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Vehicle>> getVehicles() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable String id) {
        Vehicle result = repository.findById(id);
        if (null == result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Vehicle> setCompleted(@PathVariable String id) {
        Vehicle result = repository.findById(id);
        if (null == result) {
            return ResponseEntity.notFound().build();
        }
        result.setModified(LocalDateTime.now());
        result.setCompleted(true);
        repository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createVehicle(@Valid @RequestBody Vehicle vehicle, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(VehicleValidationErrorFactory.fromBindingErrors(errors));
        }
        Vehicle result = repository.save(vehicle);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Vehicle> deleteVehicle(@RequestBody Vehicle vehicle) {
        repository.delete(vehicle);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deleteVehicle(@PathVariable String id) {
        repository.delete(VehicleBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public VehicleValidationError handleException(Exception exception) {
        return new VehicleValidationError(exception.getMessage());
    }
}
