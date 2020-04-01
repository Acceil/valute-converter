package org.itstep.msk.app.configuration;

import jdk.vm.ci.meta.ExceptionHandler;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;

public class CommonConfigurationTest {
    @Test
    public void passwordEncoder() {
        // Arrange
        CommonConfiguration config = new CommonConfiguration();

        // Act
        PasswordEncoder result = config.passwordEncoder();

        // Assert
        Assert.assertTrue(result instanceof BCryptPasswordEncoder);
    }

    @Test(expected = Exception.class)
    public void whenExceptionThrown_thenExpectationSatisfied() {
        String test = null;
        test.length();
    }
}
