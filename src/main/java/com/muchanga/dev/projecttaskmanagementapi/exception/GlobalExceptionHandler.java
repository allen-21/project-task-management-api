package com.muchanga.dev.projecttaskmanagementapi.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.muchanga.dev.projecttaskmanagementapi.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //login existe
    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleLoginAlreadyExists(
            LoginAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    //login ou passowrd errados
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO("Login or password is incorrect"));
    }

    // token JWT inválido ou expirado
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidateToken(){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO("Invalid token"));

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidateError(
            MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(message));

    }

    // qualquer outra exceção não tratada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(new ErrorResponseDTO("Erro interno do servidor"));
    }



}
