package ca.retrylife.legalaccess;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utilities for private access
 */
public class AccessUtil {

    /**
     * Fetches a private field from an object
     * 
     * @param fieldName Name of the field
     * @param owner     Object to fetch from
     * @return Value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static Object accessField(String fieldName, Object owner)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

        // Access fields
        Field privateField = owner.getClass().getDeclaredField(fieldName);
        privateField.setAccessible(true);

        // Fetch from JVM
        return privateField.get(owner);
    }

    /**
     * Fetches a private field from an object, or returns NULL if there was an error
     * 
     * @param fieldName Name of the field
     * @param owner     Object to fetch from
     * @return Value or NULL
     */
    public static Object accessFieldOrNull(String fieldName, Object owner) {
        try {
            return accessField(fieldName, owner);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return null;
        }
    }

    /**
     * Call a private method from an object
     * 
     * @param methodName Name of the method
     * @param owner      Object that owns the method
     * @param args       Method arguments
     * @return Method output
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object callMethod(String methodName, Object owner, Object... args) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // Determine parameter types
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        // Access method
        Method privateMethod = owner.getClass().getDeclaredMethod(methodName, argTypes);
        privateMethod.setAccessible(true);

        // Make method call
        return privateMethod.invoke(owner, args);
    }

    /**
     * Call a private method from an object or return NULL if there was an error
     * 
     * @param methodName Name of the method
     * @param owner      Object that owns the method
     * @param args       Method arguments
     * @return Method output or NULL
     */
    public static Object callMethodOrNull(String methodName, Object owner, Object... args) {
        try {
            return callMethod(methodName, owner, args);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            return null;
        }
    }

}