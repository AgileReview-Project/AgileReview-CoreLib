/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.common.parser;

import java.io.InputStream;
import java.util.Properties;

import org.agilereview.common.AbstractProperties;

/**
 * This class enables access to all configurable parser related properties
 * @author Malte Brunnlieb (18.05.2014)
 */
public class ParserProperties extends AbstractProperties {
    
    /**
     * @param properties
     * @author Malte Brunnlieb (19.05.2014)
     */
    private ParserProperties(InputStream properties) {
        super(properties);
    }
    
    public static Properties newInstance() {
        ParserProperties parserProperties = new ParserProperties(ParserProperties.class.getResourceAsStream("/resources/parser.properties"));
        return parserProperties.loadProperties();
    }
    
    /**
     * Key Separator for deprecated review / author / comment key notation
     */
    public final static String KEY_SEPARATOR = "parser.keySeparator";
    
    /**
     * Marker sign to distinguish start and end tags from each other
     */
    public final static String START_END_TAG_MARKER_SIGN = "parser.startEndTagMarkerSign";
    
    /**
     * Marker sign to mark the commented line to be removed after comment removal
     */
    public final static String LINE_REMOVAL_MARKER_SIGN = "parser.lineRemovalMarkerSign";
}
