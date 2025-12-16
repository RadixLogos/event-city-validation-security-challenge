package com.devsuperior.bds04.dto.errors;

import com.devsuperior.bds04.dto.FieldMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError{
    List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timeStamp, Integer status, String error, String path) {
        super(timeStamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addErrors(String field, String message){
        errors.removeIf(x -> x.fieldName().equals(field));
        errors.add(new FieldMessage(field,message));
    }
}
