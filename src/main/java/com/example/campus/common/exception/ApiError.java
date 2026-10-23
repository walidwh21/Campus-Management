package com.example.campus.common.exception;

import java.time.Instant;

/** Uniform JSON error body returned by every failing endpoint. */
public record ApiError(Instant timestamp, int status, String error, String message) {
    public static ApiError of(int status, String error, String message) {
        return new ApiError(Instant.now(), status, error, message);
    }
}
