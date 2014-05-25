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
        Assert.assertEquals(getLines(testResource, 15, 19), getLines(tmpFile, 15, 19));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 18, 19);
        
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
        Assert.assertEquals(getLines(testResource, 15, 19), getLines(tmpFile, 15, 19));
        
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
        Assert.assertEquals(getLines(testResource, 15, 19), getLines(tmpFile, 15, 19));
        
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
     * Tests adding a multi line comment, where the first line should be adapted and the the adapted line contains contents
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsMultiLineComments_startLineAdaptedAndLineInserted_code() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "TAGID";
        
        //precondition
        Assert.assertEquals(getLines(testResource, 29, 33), getLines(tmpFile, 29, 33));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 30, 32);
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isMultilineStartTag();
        tagBuilder.cleanupLineWithCommentRemoval(true);
        String startTag = tagBuilder.buildTag(tagId);
        tagBuilder.isMultilineEndTag();
        tagBuilder.cleanupLineWithCommentRemoval(false);
        String endTag = tagBuilder.buildTag(tagId);
        
        Assert.assertEquals("    public String a;\n" + startTag + "\n    /**\n     * JavaDoc\n     */" + endTag, getLines(tmpFile, 29, 33));
    }
    
    /**
     * Tests adding a multi line comment, where the first line should be adapted and the the adapted line is end of multi-line comment
     * @throws URISyntaxException
     * @throws IOException
     * @author Malte Brunnlieb (24.05.2014)
     */
    @Test
    public void testAddTagsMultiLineComments_startLineAdaptedAndLineInserted_comment() throws URISyntaxException, IOException {
        File testResource = new File(getClass().getResource("/resources/TestClass.java").toURI());
        File tmpFile = File.createTempFile("TestClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "TAGID";
        
        //precondition
        Assert.assertEquals(getLines(testResource, 37, 41), getLines(tmpFile, 37, 41));
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.addTags(tagId, 38, 40);
        
        //assertions
        CommentTagBuilder tagBuilder = new CommentTagBuilder("/*", "*/");
        tagBuilder.isMultilineStartTag();
        tagBuilder.cleanupLineWithCommentRemoval(true);
        String startTag = tagBuilder.buildTag(tagId);
        tagBuilder.isMultilineEndTag();
        tagBuilder.cleanupLineWithCommentRemoval(false);
        String endTag = tagBuilder.buildTag(tagId);
        
        Assert.assertEquals("     */\n" + startTag + "\n    /**\n     * JavaDoc\n     */" + endTag, getLines(tmpFile, 37, 41));
    }
    
    /**
     * Tests removal of a single line comment
     * @throws IOException
     * @throws URISyntaxException
     * @author Malte Brunnlieb (25.05.2014)
     */
    @Test
    public void testRemoveTag_singleLine() throws IOException, URISyntaxException {
        File testResource = new File(getClass().getResource("/resources/CommentedClass.java").toURI());
        File tmpFile = File.createTempFile("CommentedClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "111";
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.removeTags(tagId);
        
        //assertions
        File targetFile = new File(getClass().getResource("/resources/CommentedClass_111removed.java").toURI());
        Assert.assertArrayEquals(FileUtils.readLines(targetFile).toArray(), FileUtils.readLines(tmpFile).toArray());
    }
    
    /**
     * Tests removal of a multi line comment with line removal
     * @throws IOException
     * @throws URISyntaxException
     * @author Malte Brunnlieb (25.05.2014)
     */
    @Test
    public void testRemoveTag_multiLine_withLineRemoval() throws IOException, URISyntaxException {
        File testResource = new File(getClass().getResource("/resources/CommentedClass.java").toURI());
        File tmpFile = File.createTempFile("CommentedClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "333";
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.removeTags(tagId);
        
        //assertions
        File targetFile = new File(getClass().getResource("/resources/CommentedClass_333removed.java").toURI());
        Assert.assertArrayEquals(FileUtils.readLines(targetFile).toArray(), FileUtils.readLines(tmpFile).toArray());
    }
    
    /**
     * Tests removal of a multi line comment
     * @throws IOException
     * @throws URISyntaxException
     * @author Malte Brunnlieb (25.05.2014)
     */
    @Test
    public void testRemoveTag_multiLine() throws IOException, URISyntaxException {
        File testResource = new File(getClass().getResource("/resources/CommentedClass.java").toURI());
        File tmpFile = File.createTempFile("CommentedClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        String tagId = "444";
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.removeTags(tagId);
        
        //assertions
        File targetFile = new File(getClass().getResource("/resources/CommentedClass_444removed.java").toURI());
        Assert.assertArrayEquals(FileUtils.readLines(targetFile).toArray(), FileUtils.readLines(tmpFile).toArray());
    }
    
    /**
     * Tests comment tag cleanup
     * @throws IOException
     * @throws URISyntaxException
     * @author Malte Brunnlieb (25.05.2014)
     */
    @Test
    public void testclearTags() throws IOException, URISyntaxException {
        File testResource = new File(getClass().getResource("/resources/CommentedClass.java").toURI());
        File tmpFile = File.createTempFile("CommentedClass", "java");
        FileUtils.copyFile(testResource, tmpFile);
        
        //execution
        FileParser parser = new FileParser(tmpFile, new String[] { "/*", "*/" });
        parser.clearAllTags();
        
        //assertions
        File targetFile = new File(getClass().getResource("/resources/CommentedClass_allRemoved.java").toURI());
        Assert.assertArrayEquals(FileUtils.readLines(targetFile).toArray(), FileUtils.readLines(tmpFile).toArray());
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
