/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.common.parser;

/**
 * Using the {@link CommentTagBuilder} you can generate a specific instance of a comment tag assuring the conventions such that every plug-in will be
 * able to interpret your tags afterwards.
 * @author Malte Brunnlieb (18.05.2014)
 */
public class CommentTagBuilder extends TagBuilder {
    
    /**
     * Determines whether the tag to be build is a start tag
     */
    protected boolean isStartTag;
    /**
     * Determines whether the tag to be build is a end tag
     */
    protected boolean isEndTag;
    /**
     * Determines whether the line should be removed on comment removal
     */
    protected boolean cleanupLineWithCommentRemoval;
    
    /**
     * Creates a new {@link CommentTagBuilder} instance
     * @param multilineCommentStartSign the multi-line start sign (e.g. /* for java)
     * @param multilineCommentEndSign the multi-line end sign (e.g. {@literal *}/ for java)
     * @author Malte Brunnlieb (18.05.2014)
     */
    public CommentTagBuilder(String multilineCommentStartSign, String multilineCommentEndSign) {
        super(multilineCommentStartSign, multilineCommentEndSign);
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
     * @param cleanup states whether the line should be cleaned or not
     * @return the Builder instance
     * @author Malte Brunnlieb (19.05.2014)
     */
    public CommentTagBuilder cleanupLineWithCommentRemoval(boolean cleanup) {
        cleanupLineWithCommentRemoval = cleanup;
        return this;
    }
    
    /**
     * Creates a new comment tag for the given configuration
     * @param tagId id of the comment
     * @return the new comment tag
     * @author Malte Brunnlieb (24.05.2014)
     */
    public String buildTag(String tagId) {
        return startTag + "-" + (isStartTag ? startEndTagMarker : "") + keySeparator + tagId + keySeparator + (isEndTag ? startEndTagMarker : "")
                + (cleanupLineWithCommentRemoval ? cleanupMarker : "") + endTag;
    }
    
}
