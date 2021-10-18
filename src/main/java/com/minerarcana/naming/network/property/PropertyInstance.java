package com.minerarcana.naming.network.property;

import org.apache.logging.log4j.Logger;

public class PropertyInstance {
    private final Logger logger;
    private final PropertyNetwork network;

    public PropertyInstance(String id, Logger logger) {
        this.logger = logger;
        this.network = new PropertyNetwork(id);
    }

    public PropertyNetwork getNetwork() {
        return network;
    }

    public Logger getLogger() {
        return logger;
    }

    public PropertyManager createManager(int containerId) {
        return new PropertyManager((short) containerId, this);
    }
}
