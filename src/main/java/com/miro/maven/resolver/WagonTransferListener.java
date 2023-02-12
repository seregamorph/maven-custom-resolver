package com.miro.maven.resolver;

import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.observers.AbstractTransferListener;
import org.eclipse.aether.spi.connector.transport.TransportListener;
import org.eclipse.aether.transfer.TransferCancelledException;

import java.nio.ByteBuffer;

/**
 * Exact copy of package-private class
 */
final class WagonTransferListener
        extends AbstractTransferListener {

    private final TransportListener listener;

    WagonTransferListener(TransportListener listener) {
        this.listener = listener;
    }

    @Override
    public void transferStarted(TransferEvent event) {
        try {
            listener.transportStarted(0, event.getResource().getContentLength());
        } catch (TransferCancelledException e) {
            /*
             * NOTE: Wagon transfers are not freely abortable. In particular, aborting from
             * AbstractWagon.fire(Get|Put)Started() would result in unclosed streams so we avoid this case.
             */
        }
    }

    @Override
    public void transferProgress(TransferEvent event, byte[] buffer, int length) {
        try {
            listener.transportProgressed(ByteBuffer.wrap(buffer, 0, length));
        } catch (TransferCancelledException e) {
            throw new WagonCancelledException(e);
        }
    }
}
