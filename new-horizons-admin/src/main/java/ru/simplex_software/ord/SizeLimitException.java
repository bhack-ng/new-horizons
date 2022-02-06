package ru.simplex_software.ord;

import java.io.IOException;

/**
 * Stream limit exceed.
 */
public class SizeLimitException extends IOException {
    public SizeLimitException(String message) {
        super(message);
    }

    public SizeLimitException() {
    }
}
