/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.common.parser;

import java.util.regex.Pattern;

/**
 * Builder for comment tag regex
 * @author Malte Brunnlieb (18.05.2014)
 */
public class CommentTagRegexBuilder extends TagBuilder {
    
    /**
     * Creates a new builder for tag regex
     * @param multilineCommentStartSign the multi-line start sign (e.g. /* for java)
     * @param multilineCommentEndSign the multi-line end sign (e.g. {@literal *}/ for java)
     * @author Malte Brunnlieb (24.05.2014)
     */
    public CommentTagRegexBuilder(String multilineCommentStartSign, String multilineCommentEndSign) {
        super(multilineCommentStartSign, multilineCommentEndSign);
    }
    
    /**
     * Builds a new tag regex for the given configuration. The following regex groups can be accessed if no new group is inserted via the tagId:<br>
     * (1) start tag marker character<br> (2) end tag marker character<br>tagId<br>(3) line removal marker character<br>
     * @param tagId tag id to be searched for
     * @param isRegex states whether the passed tagId is already a regex
     * @return the built tag regex
     * @author Malte Brunnlieb (24.05.2014)
     */
    public String buildTagRegex(String tagId, boolean isRegex) {
        String quotedKeysepartor = Pattern.quote(keySeparator);
        String quotedStartEndTagMarker = Pattern.quote(startEndTagMarker);
        String searchRegex = isRegex ? tagId : Pattern.quote(tagId);
        return Pattern.quote(startTag) + "-?(" + quotedStartEndTagMarker + ")?" + quotedKeysepartor + searchRegex + quotedKeysepartor + "("
                + quotedStartEndTagMarker + ")?(" + Pattern.quote(cleanupMarker) + ")?" + Pattern.quote(endTag);
    }
    
    /**
     * Builds a new tag regex for the given configuration. The following regex groups can be accessed if no new group is inserted via the tagId:<br>
     * (1) start tag marker character<br> (2) end tag marker character<br>(3) tag id<br>(4) line removal marker character<br>
     * @return the built tag regex
     * @author Malte Brunnlieb (24.05.2014)
     */
    public String buildTagRegex() {
        return buildTagRegex("(.+?)", true);
    }
    
}
