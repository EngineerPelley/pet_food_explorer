package com.petfood.explorer.error;

import java.time.OffsetDateTime;

/**
 * Consistent JSON error body returned for every handled exception.
 */
public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path) {
}
