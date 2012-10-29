package org.mule.plugin;

public class A {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String packageName = "org.mule.test.facke";
        System.out.println(packageName.replaceAll("\\.", "/"));

    }
}
