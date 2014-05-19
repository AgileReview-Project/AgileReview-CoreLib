/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.common.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.agilereview.common.configuration.ConfigurationKeys;
import org.agilereview.common.exception.CommonPropertiesTechnicalRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MessageSourceHolder} gives access to error messages configured in property files.
 * @author Malte Brunnlieb (19.05.2014)
 */
public class MessageSourceHolder {
    
    /**
     * Logger instance
     */
    private static Logger LOG = LoggerFactory.getLogger(MessageSourceHolder.class);
    
    /**
     * All loaded Messages
     */
    private Properties messages = new Properties();
    
    /**
     * Creates a new MessageSourceHolder for resolving messages from property files
     * @param classPathResourcePaths
     * @author Malte Brunnlieb (19.05.2014)
     */
    public MessageSourceHolder(String... classPathResourcePaths) {
        Properties globalProperties = new Properties();
        try {
            globalProperties.load(MessageSourceHolder.class.getResourceAsStream("/resources/global.properties"));
        } catch (IOException e1) {
            throw new CommonPropertiesTechnicalRuntimeException();
        }
        String locale = globalProperties.getProperty(ConfigurationKeys.LOCALE);
        
        for (String path : classPathResourcePaths) {
            Properties resMessages = new Properties();
            String fullPath = path + "_" + locale.toLowerCase() + ".properties";
            LOG.debug("Load Property file {}", fullPath);
            try (InputStream is = MessageSourceHolder.class.getResourceAsStream(fullPath)) {
                resMessages.load(is);
            } catch (IOException e) {
                throw new CommonPropertiesTechnicalRuntimeException();
            }
            messages.putAll(resMessages);
        }
    }
    
    /**
     * Returns the message to the given key or <code>null</code> if there is no message configured for the given key
     * @param key lookup key
     * @return the message configured or <code>null</code> if there is no message configured
     * @author Malte Brunnlieb (19.05.2014)
     */
    public String getMessage(String key) {
        return messages.getProperty(key);
    }
}
