package com.Showroom.Service.ServiceMangement.Exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> conflict(DataIntegrityViolationException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, "The record conflicts with an existing record or a database constraint"));
    }
}