/**
 * Mule Class Extender Maven Plugin
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.plugin;

public class B extends A{
    /**
     * @param args
     */
    public static void main(String[] args) {
        String packageName = "org.mule.test.facke";
        System.out.println(packageName.replaceAll("\\.", "/"));

    }
}
