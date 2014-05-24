/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.common.parser;

import java.util.Properties;

/**
 * Builder providing all necessary properties
 * @author Malte Brunnlieb (24.05.2014)
 */
public abstract class TagBuilder {
    
    /**
     * Start / End - tag marker
     */
    protected String startEndTagMarker;
    /**
     * Key separator for separating tags from the tag id
     */
    protected String keySeparator;
    /**
     * Cleanup marker sign, which marks a line to be removed after comment removal
     */
    protected String cleanupMarker;
    
    /**
     * Multi-line comment start sign
     */
    protected final String startTag;
    /**
     * Multi-line comment end sign
     */
    protected final String endTag;
    
    /**
     * Creates a new builder providing all necessary properties
     * @param multilineCommentStartSign the multi-line start sign (e.g. /* for java)
     * @param multilineCommentEndSign the multi-line end sign (e.g. {@literal *}/ for java)
     * @author Malte Brunnlieb (24.05.2014)
     */
    public TagBuilder(String multilineCommentStartSign, String multilineCommentEndSign) {
        startTag = multilineCommentStartSign;
        endTag = multilineCommentEndSign;
        
        Properties parserProperties = ParserProperties.newInstance();
        startEndTagMarker = parserProperties.getProperty(ParserProperties.START_END_TAG_MARKER_SIGN);
        keySeparator = parserProperties.getProperty(ParserProperties.KEY_SEPARATOR);
        cleanupMarker = parserProperties.getProperty(ParserProperties.LINE_REMOVAL_MARKER_SIGN);
    }
    
}
