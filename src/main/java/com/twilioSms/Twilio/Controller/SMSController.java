package com.twilioSms.Twilio.Controller;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilioSms.Twilio.Payload.SMSRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSController {

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestBody SMSRequest smsRequest) {
        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                            new PhoneNumber(smsRequest.getTo()),
                            new PhoneNumber(twilioPhoneNumber),
                            smsRequest.getMessage())
                    .create();

            System.out.println("SMS sent successfully with SID: " + message.getSid());
            return ResponseEntity.ok("SMS sent Successfully. SID: " + message.getSid());
        } catch (ApiException e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            // Handle the exception as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send SMS: " + e.getMessage());
        }
    }
}
