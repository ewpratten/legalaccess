package ca.retrylife.legalaccess;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AccessorTest {

    private class TestClass {
        private boolean testValueTrue = true;
    }
    
    @Test
    public void testProperAccess() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        // Access the test value
        TestClass clazz = new TestClass();
        Accessor<Boolean> a = new Accessor<>("testValueTrue", clazz, Boolean.class);

        // Fetch the value
        assertTrue("Value", a.get());
        
    }
}