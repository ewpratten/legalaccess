package ca.retrylife.legalaccess;

/**
 * Utilities for comparing class types
 */
public class ClassTypeComparisonUtil {

    // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
    private static String[] knownMatchingTypes = new String[] { "byte", "short", "int", "long", "float", "double",
            "char", "boolean" };

    /**
     * Check if two classes represent the same data
     * 
     * @param class1 Class 1
     * @param class2 Class 2
     * @return Are same type?
     */
    public static boolean matches(Class<?> class1, Class<?> class2) {

        // Get names
        String[] class1Name = class1.getName().split("[.]");
        String[] class2Name = class2.getName().split("[.]");

        // Check if the classes are primitive-ish
        if (class1Name[class1Name.length - 1].toLowerCase().equals(class2Name[class2Name.length - 1].toLowerCase())) {
            for (String matchingType : knownMatchingTypes) {
                if (class1Name[class1Name.length - 1].toLowerCase().equals(matchingType)) {
                    return true;
                }
            }
        }

        // If these are seperate classes, or not in the known types, handle
        return class1.equals(class2);

    }

}