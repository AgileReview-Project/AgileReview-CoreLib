/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.fileparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.agilereview.common.parser.CommentTagBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test-Class for the {@link FileParser}
 * @author Malte Brunnlieb (18.05.2014)
 */
public class FileParserTest {
    
    /**
     * Tests adding a single line comment
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsSingleLineComments() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        
        //precondition
        Assert.assertEquals("        System.out.println(\"am\");", getLine(tmpFile, 24));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags("TAGID", 24, 24);
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isSingleLine();
        Assert.assertEquals("        System.out.println(\"am\");" + tagBuilder.buildTag("TAGID"), getLine(tmpFile, 24));
        
    }
    
    /**
     * Tests adding a multi line comment
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsMultiLineComments() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "TAGID";
        
        //precondition
        Assert.assertEquals(getLines(testResource, 23, 25), getLines(tmpFile, 23, 25));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 23, 25);
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isMultilineStartTag();
        String startTag = tagBuilder.buildTag(tagId);
        tagBuilder.isMultilineEndTag();
        String endTag = tagBuilder.buildTag(tagId);
        Assert.assertEquals("        System.out.println(\"I\");" + startTag
                + "\n        System.out.println(\"am\");\n        System.out.println(\"a\");" + endTag, getLines(tmpFile, 23, 25));
        
    }
    
    /**
     * Tests adding a multi line comment, where the first line should be adapted as this line is within a comment
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsMultiLineComments_firstLineAdapted() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "TAGID";
        
        //precondition
        Assert.assertEquals(getLines(testResource, 18, 19), getLines(tmpFile, 18, 19));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 18, 19);
        
        System.out.println(FileUtils.readFileToString(tmpFile));
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isMultilineStartTag();
        String startTag = tagBuilder.buildTag(tagId);
        tagBuilder.isMultilineEndTag();
        String endTag = tagBuilder.buildTag(tagId);
        Assert.assertEquals("    " + startTag + "\n    /**\n     * @param args\n     * @author Malte Brunnlieb (18.05.2014)\n     */" + endTag,
                getLines(tmpFile, 15, 19));
        
    }
    
    /**
     * Tests adding a multi line comment, where the last line should be adapted as this line is within a comment
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsMultiLineComments_lastLineAdapted() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "TAGID";
        
        //precondition
        Assert.assertEquals(getLines(testResource, 15, 17), getLines(tmpFile, 15, 17));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 15, 17);
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isMultilineStartTag();
        String startTag = tagBuilder.buildTag(tagId);
        tagBuilder.isMultilineEndTag();
        String endTag = tagBuilder.buildTag(tagId);
        Assert.assertEquals("    " + startTag + "\n    /**\n     * @param args\n     * @author Malte Brunnlieb (18.05.2014)\n     */" + endTag,
                getLines(tmpFile, 15, 19));
        
    }
    
    /**
     * Tests adding a multi line comment, where both lines should be adapted as these lines are within a comment
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsMultiLineComments_bothLinesAdapted() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "TAGID";
        
        //precondition
        Assert.assertEquals(getLines(testResource, 17, 18), getLines(tmpFile, 17, 18));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 17, 18);
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isMultilineStartTag();
        String startTag = tagBuilder.buildTag(tagId);
        tagBuilder.isMultilineEndTag();
        String endTag = tagBuilder.buildTag(tagId);
        Assert.assertEquals("    " + startTag + "\n    /**\n     * @param args\n     * @author Malte Brunnlieb (18.05.2014)\n     */" + endTag,
                getLines(tmpFile, 15, 19));
        
    }
    
    /**
     * Returns the requested line interval by connecting the lines with \n line breaks
     * @param file File to get the lines from
     * @param from first line to retrieve
     * @param to last line to retrieve
     * @return returns the requested lines
     * @author Malte Brunnlieb (24.05.2014)
     */
    private String getLines(File file, int from, int to) {
        StringBuffer lines = new StringBuffer();
        try (FileReader fileReader = new FileReader(file); BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String line = null;
            int currLineNr = 0;
            while ((line = reader.readLine()) != null) {
                currLineNr++;
                if (from <= currLineNr) {
                    if (lines.length() != 0) lines.append("\n");
                    lines.append(line);
                }
                if (to == currLineNr) break;
            }
        } catch (Exception e) {
        }
        return lines.toString();
    }
    
    /**
     * Returns the line with the given number from the given file
     * @param file {@link File} to get the line from
     * @param lineNr line number to be retrieved
     * @return the line with the given number
     * @author Malte Brunnlieb (24.05.2014)
     */
    private String getLine(File file, int lineNr) {
        try (FileReader fileReader = new FileReader(file); BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String line = null;
            int currLineNr = 0;
            while ((line = reader.readLine()) != null) {
                currLineNr++;
                if (lineNr == currLineNr) return line;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
