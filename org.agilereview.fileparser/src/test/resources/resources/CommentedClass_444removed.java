/**
 * Copyright (c) 2011, 2012 AgileReview Development Team and others.
 * All rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License - v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Malte Brunnlieb, Philipp Diebold, Peter Reuter, Thilo Rauch
 */
package org.agilereview.fileparser.impl;

/**
 * 
 * @author Malte Brunnlieb (18.05.2014)
 */
public class TestClass {/*-?|111|?*/
    
    /**
     * @param args
     * @author Malte Brunnlieb (18.05.2014)
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        System.out.println("I");
        System.out.println("am");/*-?|222|?*/
        System.out.println("a");
        System.out.println("Test");
    }
    
    public String a;
    /*-?|333|-*/
    /**
     * JavaDoc
     *//*-|333|?*/
    public String b;

    /*
     * Comment
     */
    /**
     * JavaDoc
     */
    public void method() {
        
    }
    
}
