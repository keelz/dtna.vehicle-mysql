package com.dtna.vehicle.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class VehicleValidationErrorFactory {

    public static VehicleValidationError fromBindingErrors(Errors errors) {
        VehicleValidationError result = new VehicleValidationError("Validation has failed. " + errors.getErrorCount() + " error(s)");
        for (ObjectError error : errors.getAllErrors()) {
            result.addValidationError(error.getDefaultMessage());
        }
        return result;
    }
}
