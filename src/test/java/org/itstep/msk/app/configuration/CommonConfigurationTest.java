package org.itstep.msk.app.configuration;

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
}
