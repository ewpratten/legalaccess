package ca.retrylife.legalaccess;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class MethodAccessorTest {

    private class TestClass {
        private boolean testMethod() {
            return true;
        }
    }

    @Test
    public void testProperAccess() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        // Access the test value
        TestClass clazz = new TestClass();
        MethodAccessor<Boolean> a = new MethodAccessor<>("testMethod", clazz, Boolean.class);

        // Fetch the value
        assertTrue("Value", a.call());

    }

    @Test
    public void testInvalidAccess() {

        // Access the test value
        TestClass clazz = new TestClass();
        MethodAccessor<Boolean> a = null;

        try {
            a = new MethodAccessor<>("testInvalidMethod", clazz, Boolean.class);
        } catch (SecurityException | NoSuchMethodException e) {
            assertTrue("Caught exception", true);
            return;
        }

        // Fetch the value
        assertNull("Value", a.callOrNull());
    }

}