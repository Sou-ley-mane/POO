package com.gie.fleet.exception;

import java.time.LocalDateTime;

public class AuthenticationLockedException extends RuntimeException {

    private final LocalDateTime debloqueA;

    public AuthenticationLockedException(String message, LocalDateTime debloqueA) {
        super(message);
        this.debloqueA = debloqueA;
    }

    public LocalDateTime getDebloqueA() {
        return debloqueA;
    }
}
