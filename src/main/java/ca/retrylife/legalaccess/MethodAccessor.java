package ca.retrylife.legalaccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper class for accessing a private method
 * 
 * @param <T> Method return type
 */
public class MethodAccessor<T> {

    private final Object owner;
    private final Method privateMethod;
    private final Class<?>[] argumentTypes;

    /**
     * Create a new MethodAccessor
     * 
     * @param methodName    Name of private method
     * @param owner         Object to get method from
     * @param classOfT      The class of generic type T
     * @param argumentTypes The type of every method type in order
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public MethodAccessor(String methodName, Object owner, Class<T> classOfT, Class<?>... argumentTypes)
            throws NoSuchMethodException, SecurityException {
        this.owner = owner;
        this.argumentTypes = argumentTypes;
        this.privateMethod = owner.getClass().getDeclaredMethod(methodName, argumentTypes);
        this.privateMethod.setAccessible(true);

        // Error if there is a type mismatch
        if (!this.privateMethod.getReturnType().equals(classOfT)) {
            throw new RuntimeException(String.format("Type mismatch between %s and return type of method %s (%s)",
                    classOfT.getName(), methodName, this.privateMethod.getReturnType().getName()));
        }
    }

    /**
     * Call the method
     * 
     * @param args Arguments to pass
     * @return Return value
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public T call(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // Ensure correct number of arguments were passed
        if (args.length != this.argumentTypes.length) {
            throw new RuntimeException(String.format("Incorrect number of method arguments. Expected %d, got %d",
                    this.argumentTypes.length, args.length));
        }

        // Ensure arguments all match
        for (int i = 0; i < args.length; i++) {
            if (!args[i].getClass().equals(this.argumentTypes[i])) {
                throw new RuntimeException(
                        String.format("Type mismatch between argument number %d (%s) and expected type: %s", i,
                                args[i].getClass().getName(), this.argumentTypes[i].getName()));
            }
        }

        // Make call
        return (T) this.privateMethod.invoke(this.owner, args);
    }

    /**
     * Call the method, or return NULL if there was an error
     * 
     * @param args Arguments to pass
     * @return Return value or NULL
     */
    public T callOrNull(Object... args) {
        try {
            return call(args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
    }

}