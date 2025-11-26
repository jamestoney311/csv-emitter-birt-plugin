package org.eclipse.birt.report.engine.emitter.csv;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * OSGi bundle activator for the CSV emitter plugin.
 */
public class CsvPlugin implements BundleActivator {

    private static BundleContext context;

    /**
     * Gets the bundle context.
     * @return the bundle context
     */
    static BundleContext getContext() {
        return context;
    }

    /**
     * Called when the bundle is started.
     * @param bundleContext the bundle context
     * @throws Exception if an error occurs
     */
    public void start(BundleContext bundleContext) throws Exception {
        CsvPlugin.context = bundleContext;
    }

    /**
     * Called when the bundle is stopped.
     * @param bundleContext the bundle context
     * @throws Exception if an error occurs
     */
    public void stop(BundleContext bundleContext) throws Exception {
        CsvPlugin.context = null;
    }

}
