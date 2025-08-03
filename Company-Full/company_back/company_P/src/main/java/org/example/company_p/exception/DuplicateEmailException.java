package org.example.company_p.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super("this email already exists: "+message);
    }
}
