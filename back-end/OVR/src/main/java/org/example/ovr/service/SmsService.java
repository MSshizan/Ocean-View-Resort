package org.example.ovr.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @Async
    public void sendSms(String toNumber, String messageBody) {
        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new com.twilio.type.PhoneNumber(toNumber),
                    new com.twilio.type.PhoneNumber(fromNumber),
                    messageBody
            ).create();

            System.out.println("✅ SMS sent successfully to " + toNumber);

        } catch (com.twilio.exception.ApiException e) {
            // Handle trial account or unverified number specifically
            if (e.getCode() == 21608) { // Twilio code for unverified number in trial
                System.err.println("⚠️ SMS not sent: Trial account limitation. Number " + toNumber + " must be verified in Twilio.");
            } else {
                System.err.println("⚠️ SMS could not be sent: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("⚠️ SMS could not be sent: " + e.getMessage());
        }
    }
}