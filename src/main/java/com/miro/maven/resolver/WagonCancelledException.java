package com.miro.maven.resolver;

import org.eclipse.aether.transfer.TransferCancelledException;

/**
 * Exact copy of package-private class
 */
class WagonCancelledException extends RuntimeException {

    WagonCancelledException(TransferCancelledException cause) {
        super(cause);
    }

    public static Exception unwrap(Exception e) {
        if (e instanceof WagonCancelledException) {
            e = (Exception) e.getCause();
        }
        return e;
    }
}
