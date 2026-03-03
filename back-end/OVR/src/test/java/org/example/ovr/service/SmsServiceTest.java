package org.example.ovr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class SmsServiceTest {

    private SmsService smsService;

    @BeforeEach
    void setUp() throws Exception {
        smsService = new SmsService();

        // Set private @Value fields via reflection
        setPrivateField(smsService, "accountSid", "ACxxxxxxxxxxxxxxxx");
        setPrivateField(smsService, "authToken", "authToken");
        setPrivateField(smsService, "fromNumber", "+1234567890");
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testSendSms() {
        // Since Twilio.init and Message.creator are static, we won't actually call them.
        // We'll just call the method to ensure no exceptions occur with our test data.

        // Example valid numbers and message
        String toNumber = "+19876543210";
        String messageBody = "Hello! This is a test SMS.";

        // Run the method (won't send actual SMS)
        smsService.sendSms(toNumber, messageBody);

        // Since this method prints to console and has no return, you can just verify
        // no exceptions occur. For real unit testing, wrap Twilio in an interface.
    }
}