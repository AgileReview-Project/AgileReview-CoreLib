/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.fileparser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.agilereview.common.parser.CommentTagBuilder;
import org.agilereview.common.parser.CommentTagRegexBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a file parser which adds AgileReview comment tags to a file
 * @author Malte Brunnlieb (18.05.2014)
 */
public class FileParser {
    
    private static Logger LOG = LoggerFactory.getLogger(FileParser.class);
    
    /**
     * New line character
     */
    private static String newline = System.getProperty("line.separator");
    
    private File file;
    
    private String[] tags;
    
    private CommentTagRegexBuilder tagRegexBuilder;
    
    /**
     * TODO (MB) JavaDoc
     * @param file
     * @param multiLineCommentTags
     * @author Malte Brunnlieb (18.05.2014)
     */
    public FileParser(File file, String[] multiLineCommentTags) {
        this.file = file;
        this.tags = multiLineCommentTags;
        tagRegexBuilder = new CommentTagRegexBuilder(tags[0], tags[1]);
    }
    
    /**
     * TODO JavaDoc
     * @param file
     * @param startLine
     * @param endLine
     * @param multiLineCommentTags
     * @author Malte Brunnlieb (18.05.2014)
     * @throws IOException
     */
    public void addTags(String tagId, int selStartLine, int selEndLine) throws IOException {
        LOG.debug("Add tags for comment with tagId '{}' to start line {} / end line {}", tagId, selStartLine, selEndLine);
        
        selStartLine--;
        selEndLine--;
        
        CommentTagBuilder tagBuilder = new CommentTagBuilder(tags[0], tags[1]);
        
        boolean startLineInserted = false, endLineInserted = false;
        int origSelStartLine = selStartLine;
        boolean[] significantlyChanged = new boolean[] { false, false };
        
        // TODO maybe better work on a stream than loading the whole file in memory
        //        LineIterator it = FileUtils.lineIterator(testResource);
        //        new ArrayDeque<>(5);
        //        while(it.hasNext()) {
        //            String line = it.nextLine();
        //            
        //        }
        List<String> lines = FileUtils.readLines(file);
        
        // check if selection needs to be adapted
        int[] newLines = computeSelectionAdapations(lines, selStartLine, selEndLine);
        if (newLines[0] != -1 || newLines[1] != -1) {
            LOG.debug("Comment starts and/or ends within a source comment -> adapt lines to start line {} / end line {}", newLines[0] + 1,
                    newLines[1] + 1);
            // adapt starting line if necessary
            if (newLines[0] != -1) {
                //                 insert new line if code is in front of javadoc / multi line comments
                String line = lines.get(newLines[0]);
                if (!line.trim().isEmpty()) {
                    lines.add(newLines[0] + 1, "");
                    selStartLine = newLines[0] + 1;
                    startLineInserted = true;
                } else {
                    selStartLine = newLines[0];
                }
                
                // only inform the user about these adaptations if he did not select the whole javaDoc
                if (origSelStartLine - 1 != selStartLine) {
                    significantlyChanged[0] = true;
                }
            }
            
            // adapt ending line if necessary
            // add a new line if a line was inserted before
            if (newLines[1] != -1) {
                selEndLine = newLines[1] + (startLineInserted ? 1 : 0);
                significantlyChanged[1] = true;
            } else {
                selEndLine += (startLineInserted ? 1 : 0);
            }
        }
        
        // add new line if start line is last line of javaDoc
        int[] adaptionLines = checkForCodeComment(lines, selStartLine);
        if (adaptionLines[0] != -1 && !lines.get(adaptionLines[0]).trim().isEmpty()) {
            lines.add(selStartLine + 1, "");
            selStartLine++;
            selEndLine++;
            startLineInserted = true;
            significantlyChanged[0] = true;
        }
        
        // add new line if end line is last line of javaDoc
        adaptionLines = checkForCodeComment(lines, selEndLine);
        if (adaptionLines[1] != -1 && lineContains(lines.get(adaptionLines[1]), "/**")) {
            String line = lines.get(selEndLine + 1);
            if (!line.trim().isEmpty()) {
                lines.add(selEndLine + 1, "");
                selEndLine++;
                endLineInserted = true;
                significantlyChanged[1] = true;
            }
        }
        
        if (selStartLine == selEndLine) {
            LOG.debug("Comment is single-line comment.");
            // Only one line is selected
            // Write tag -> get start+end-tag for current file-ending, insert into file
            tagBuilder.isSingleLine();
            if (startLineInserted || endLineInserted) {
                tagBuilder.cleanupLineWithCommentRemoval(true);
            } else {
                tagBuilder.cleanupLineWithCommentRemoval(false);
            }
            String line = lines.remove(selStartLine);
            line += tagBuilder.buildTag(tagId);
            lines.add(selStartLine, line);
        } else {
            LOG.debug("Comment is multi-line comment.");
            // Write tags -> get tags for current file-ending, insert second tag, insert first tag
            tagBuilder.isMultilineEndTag();
            if (endLineInserted) {
                tagBuilder.cleanupLineWithCommentRemoval(true);
            } else {
                tagBuilder.cleanupLineWithCommentRemoval(false);
            }
            String line = lines.remove(selEndLine);
            line += tagBuilder.buildTag(tagId);
            lines.add(selEndLine, line);
            
            tagBuilder.isMultilineStartTag();
            if (startLineInserted) {
                tagBuilder.cleanupLineWithCommentRemoval(true);
            } else {
                tagBuilder.cleanupLineWithCommentRemoval(false);
            }
            line = lines.remove(selStartLine);
            line += tagBuilder.buildTag(tagId);
            lines.add(selStartLine, line);
        }
        
        LOG.debug("Write file back.");
        FileUtils.writeLines(file, lines);
        
        //        parseInput();
        
        // ##########################################################
        
        //        try (FileReader fileReader = new FileReader(file); BufferedReader reader = new BufferedReader(new FileReader(file));) {
        //            StringBuilder contents = new StringBuilder();
        //            boolean isMultiLineComment = (startLine != endLine);
        //            Matcher matcher;
        //            
        //            String line;
        //            int lineNr = 0;
        //            while ((line = reader.readLine()) != null) {
        //                lineNr++;
        //                matcher = tagPattern.matcher(line);
        //                if (lineNr == startLine) {
        //                    if (isMultiLineComment) {
        //                        tagBuilder.isMultilineStartTag().
        //                    } else {
        //                        
        //                    }
        //                }
        //                if (lineNr != 0) {
        //                    contents.append(newline);
        //                }
        //                contents.append(line);
        //            }
        //            
        //            // write the new String with the replaced line OVER the same file
        //            FileOutputStream output = new FileOutputStream(file);
        //            output.write(contents.toString().getBytes());
        //            output.close();
        //        } catch (Exception e) {
        //            // TODO: handle exception
        //        }
    }
    
    /**
     * Checks whether adding an AgileReview comment at the current selection would destroy a code comment and computes adapted line numbers to avoid
     * destruction of code comments.
     * @param startLine the current startLine of the selection
     * @param endLine the current endLine of the selection
     * @return and array containing the new start (position 0) and endline (position 1). If not nothing is to be changed the content is -1 at position
     *         0/1.
     * @author Malte Brunnlieb (19.05.2014)
     */
    private int[] computeSelectionAdapations(List<String> lines, int startLine, int endLine) {
        int[] result = { -1, -1 };
        int[] startLineAdaptions = checkForCodeComment(lines, startLine);
        int[] endLineAdaptions = checkForCodeComment(lines, endLine);
        
        // check if inserting a AgileReview comment at selected code region destroys a code comment
        if (startLineAdaptions[0] != -1 && startLineAdaptions[1] != -1 && startLineAdaptions[0] != startLine) {
            result[0] = startLineAdaptions[0];
        }
        if (endLineAdaptions[0] != -1 && endLineAdaptions[1] != -1 && endLineAdaptions[1] != endLine) {
            result[1] = endLineAdaptions[1];
        }
        
        return result;
    }
    
    /**
     * Checks whether the given line is within a code comment. If this holds the code comments start and endline is returned, else {-1, -1}.
     * @param line the line to check
     * @param tags the start and endtag of code comments
     * @return [-1, -1] if line is not within a code comment, else [startline, endline] of the code comment
     */
    private int[] checkForCodeComment(List<String> lines, int line) {
        // TODO: optimize the search for tags
        
        int openTagLine = -1;
        int closeTagLine = -1;
        
        // check for opening non-AgileReview comment tags before the line
        for (int i = 0; i <= line; i++) {
            if (lineContains(lines.get(i), tags[0])) {
                openTagLine = i;
            }
        }
        
        // check for according closing non-AgileReview comment tag
        if (openTagLine > -1) {
            for (int i = openTagLine; i < lines.size(); i++) {
                if (lineContains(lines.get(i), tags[1])) {
                    closeTagLine = i;
                    break;
                }
            }
        }
        
        // finally return the results if a comment was found
        int[] result = { -1, -1 };
        if (openTagLine <= line && line <= closeTagLine) {
            // TODO: not checked if line right before starting line of code comment contains also a code comment...
            result[0] = openTagLine - 1;
            if (!(closeTagLine == line)) {
                result[1] = closeTagLine;
            }
        }
        return result;
    }
    
    /**
     * Checks whether the line identified by the lineNumber contains the given string. This function erases all AgileReview related comment tags
     * before searching for the given string.
     * @param linenumber line number of the document
     * @param string string to be searched for
     * @return true, if the string is contained in the given line ignoring AgileReview tags,<br> false otherwise
     * @author Malte Brunnlieb (08.09.2012)
     */
    private boolean lineContains(String line, String string) {
        line = line.replaceAll(tagRegexBuilder.buildTagRegex(), "");
        return line.contains(string);
    }
    
    /**
     * TODO JavaDoc
     * @param file
     * @param tagId
     * @param multiLineCommentTags
     * @author Malte Brunnlieb (18.05.2014)
     */
    public void removeTags(File file, String tagId, String[] multiLineCommentTags) {
        
    }
    
    /**
     * TODO JavaDoc
     * @param file
     * @param multiLineCommentTags
     * @author Malte Brunnlieb (18.05.2014)
     */
    public void clearAllTags(File file, String[] multiLineCommentTags) {
        
    }
    
}
