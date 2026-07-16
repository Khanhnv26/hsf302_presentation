package org.ats.exceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message); // Gọi đến constructor của RuntimeExceptio
    }
}
