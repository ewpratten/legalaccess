package ca.retrylife.legalaccess;

import java.lang.reflect.Field;

/**
 * Wrapper class for accessing a private variable
 * 
 * @param <T> Variable type
 */
public class Accessor<T> {

    private final Field privateField;
    private final Object owner;

    /**
     * Create an Accessor
     * 
     * @param fieldName Name of the private field
     * @param owner     Object to fetch from
     * @param classOfT  The class of generic type T
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public Accessor(String fieldName, Object owner, Class<T> classOfT) throws NoSuchFieldException, SecurityException {
        this.owner = owner;
        this.privateField = owner.getClass().getDeclaredField(fieldName);
        this.privateField.setAccessible(true);

        // Error if there is a type mismatch
        if (!ClassTypeComparisonUtil.matches(this.privateField.getType(), classOfT)) {
            throw new RuntimeException(String.format("Type mismatch between generic %s and type of field %s (%s)",
                    classOfT.getName(), fieldName, privateField.getType().getName()));
        }
    }

    /**
     * Create an Accessor for a static field
     * 
     * @param fieldName Name of the private static field
     * @param ownerClass Class that wraps the static field
     * @param classOfT The class of generic type T
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public Accessor(String fieldName, Class<?> ownerClass, Class<T> classOfT) throws NoSuchFieldException, SecurityException {
        this.owner = null;
        this.privateField = ownerClass.getDeclaredField(fieldName);
        this.privateField.setAccessible(true);

        // Error if there is a type mismatch
        if (!ClassTypeComparisonUtil.matches(this.privateField.getType(), classOfT)) {
            throw new RuntimeException(String.format("Type mismatch between generic %s and type of field %s (%s)",
                    classOfT.getName(), fieldName, privateField.getType().getName()));
        }
    }

    /**
     * Set the variable value
     * 
     * @param value Value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void set(T value) throws IllegalArgumentException, IllegalAccessException {
        privateField.set(this.owner, value);
    }

    /**
     * Set the variable value
     * 
     * @param value Value
     * @return True if successful
     */
    public boolean setOrFailQuietly(T value) {
        try {
            set(value);
            return true;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return false;
        }
    }

    /**
     * Get the variable value
     * 
     * @return Value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public T get() throws IllegalArgumentException, IllegalAccessException {
        return (T) privateField.get(this.owner);
    }

    /**
     * Get the variable value, or NULL on error
     * 
     * @return Value or NULL
     */
    public T getOrNull() {
        try {
            return get();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }
}