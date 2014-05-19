/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.agilereview.common.exception.CommonPropertiesTechnicalRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Property-Accessor implementation
 * @author Malte Brunnlieb (19.05.2014)
 */
public abstract class AbstractProperties {
    
    /**
     * Logger implementation
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProperties.class);
    
    /**
     * Properties input Stream
     */
    private InputStream propertiesInputStream;
    
    /**
     * 
     * @param propertiesInputStream
     * @author Malte Brunnlieb (19.05.2014)
     */
    public AbstractProperties(InputStream propertiesInputStream) {
        this.propertiesInputStream = propertiesInputStream;
    }
    
    /**
     * Returns a new instance of the {@link Properties} for accessing all parser related configuration
     * @return a new instance of the parser {@link Properties}
     * @author Malte Brunnlieb (18.05.2014)
     */
    public Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = propertiesInputStream) {
            properties.load(stream);
        } catch (IOException e) {
            LOG.debug("Could not load properties");
            throw new CommonPropertiesTechnicalRuntimeException();
        }
        return properties;
    }
}
