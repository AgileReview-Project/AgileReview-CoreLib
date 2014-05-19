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
 * Using the {@link CommentTagBuilder} you can generate a specific instance of a comment tag assuring the conventions such that every plug-in will be
 * able to interpret your tags afterwards.
 * @author Malte Brunnlieb (18.05.2014)
 */
public class CommentTagBuilder {
    
    /**
     * Start / End - tag marker
     */
    private String startEndTagMarker;
    /**
     * Key separator for separating tags from the tag id
     */
    private String keySeparator;
    /**
     * Cleanup marker sign, which marks a line to be removed after comment removal
     */
    private String cleanupMarker;
    
    /**
     * Multi-line comment start sign
     */
    private final String startTag;
    /**
     * Multi-line comment end sign
     */
    private final String endTag;
    
    /**
     * Determines whether the tag to be build is a start tag
     */
    private boolean isStartTag;
    /**
     * Determines whether the tag to be build is a end tag
     */
    private boolean isEndTag;
    /**
     * Determines whether the line should be removed on comment removal
     */
    private boolean cleanupLineWithCommentRemoval;
    
    /**
     * Creates a new {@link CommentTagBuilder} instance
     * @param multilineCommentStartSign the multi-line start sign (e.g. /* for java)
     * @param multilineCommentEndSign the multi-line end sign (e.g. {@literal *}/ for java)
     * @author Malte Brunnlieb (18.05.2014)
     */
    private CommentTagBuilder(String multilineCommentStartSign, String multilineCommentEndSign) {
        startTag = multilineCommentStartSign;
        endTag = multilineCommentEndSign;
        
        Properties parserProperties = ParserProperties.newInstance();
        startEndTagMarker = parserProperties.getProperty(ParserProperties.START_END_TAG_MARKER_SIGN);
        keySeparator = parserProperties.getProperty(ParserProperties.KEY_SEPARATOR);
        cleanupMarker = parserProperties.getProperty(ParserProperties.LINE_REMOVAL_MARKER_SIGN);
    }
    
    /**
     * Configures the builder to produce multi-line start tags
     * @return the Builder instance
     * @author Malte Brunnlieb (19.05.2014)
     */
    public CommentTagBuilder isMultilineStartTag() {
        isStartTag = true;
        isEndTag = false;
        return this;
    }
    
    /**
     * Configures the builder to produce multi-line end tags
     * @return the Builder instance
     * @author Malte Brunnlieb (19.05.2014)
     */
    public CommentTagBuilder isMultilineEndTag() {
        isStartTag = false;
        isEndTag = true;
        return this;
    }
    
    /**
     * Configures the builder to produce single line comments
     * @return the Builder instance
     * @author Malte Brunnlieb (19.05.2014)
     */
    public CommentTagBuilder isSingleLine() {
        isStartTag = true;
        isEndTag = true;
        return this;
    }
    
    /**
     * Configures the builder to produce tags with a line-cleanup marker, such that the whole line will be removed on comment removal
     * @return the Builder instance
     * @author Malte Brunnlieb (19.05.2014)
     */
    public CommentTagBuilder cleanupLineWithCommentRemoval() {
        cleanupLineWithCommentRemoval = true;
        return this;
    }
    
    /**
     * Creates a tag String dependent on the current builder configuration
     * @param tagId id of the tag, to identify the comment itself
     * @return the built tag String
     * @author Malte Brunnlieb (19.05.2014)
     */
    public String buildTag(String tagId) {
        return startTag + "-" + (isStartTag ? startEndTagMarker : "") + keySeparator + tagId + keySeparator + (isEndTag ? startEndTagMarker : "")
                + (cleanupLineWithCommentRemoval ? cleanupMarker : "") + endTag;
    }
}
