package com.miro.maven.resolver;

import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.transport.Transporter;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.locator.Service;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.transfer.NoTransporterException;
import org.eclipse.aether.transport.wagon.WagonConfigurator;
import org.eclipse.aether.transport.wagon.WagonProvider;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Copy of org.eclipse.aether.transport.wagon.WagonTransporter (final, that's why not extended) from
 * "org.apache.maven.resolver:maven-resolver-transport-wagon:1.6.3" with minor changes:
 * <ul>
 * <li>higher priority</li>
 * <li>custom FailSafeWagonTransporter</li>
 * </ul>
 */
@Named("fail-safe-wagon")
public final class FailSafeWagonTransporterFactory implements TransporterFactory, Service {

    private WagonProvider wagonProvider;

    private WagonConfigurator wagonConfigurator;

    // it should be more than priority of WagonTransporterFactory (-1.0f)
    private float priority = 1.0f;

    /**
     * Creates an (uninitialized) instance of this transporter factory. <em>Note:</em> In case of manual instantiation
     * by clients, the new factory needs to be configured via its various mutators before first use or runtime errors
     * will occur.
     */
    public FailSafeWagonTransporterFactory() {
        // enables default constructor
    }

    @Inject
    FailSafeWagonTransporterFactory(WagonProvider wagonProvider, WagonConfigurator wagonConfigurator) {
        setWagonProvider(wagonProvider);
        setWagonConfigurator(wagonConfigurator);
    }

    @Override
    public void initService(ServiceLocator locator) {
        setWagonProvider(locator.getService(WagonProvider.class));
        setWagonConfigurator(locator.getService(WagonConfigurator.class));
    }

    /**
     * Sets the wagon provider to use to acquire and release wagon instances.
     *
     * @param wagonProvider The wagon provider to use, may be {@code null}.
     * @return This factory for chaining, never {@code null}.
     */
    public FailSafeWagonTransporterFactory setWagonProvider(WagonProvider wagonProvider) {
        this.wagonProvider = wagonProvider;
        return this;
    }

    /**
     * Sets the wagon configurator to use to apply provider-specific configuration to wagon instances.
     *
     * @param wagonConfigurator The wagon configurator to use, may be {@code null}.
     * @return This factory for chaining, never {@code null}.
     */
    public FailSafeWagonTransporterFactory setWagonConfigurator(WagonConfigurator wagonConfigurator) {
        this.wagonConfigurator = wagonConfigurator;
        return this;
    }

    @Override
    public float getPriority() {
        return priority;
    }

    /**
     * Sets the priority of this component.
     *
     * @param priority The priority.
     * @return This component for chaining, never {@code null}.
     */
    public FailSafeWagonTransporterFactory setPriority(float priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public Transporter newInstance(RepositorySystemSession session, RemoteRepository repository)
            throws NoTransporterException {
        return new FailSafeWagonTransporter(wagonProvider, wagonConfigurator, repository, session);
    }

}
