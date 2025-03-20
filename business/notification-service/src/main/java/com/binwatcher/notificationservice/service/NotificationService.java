package com.binwatcher.notificationservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.notificationservice.client.BinClient;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    private final MailSenderService mailSenderService;
    private final BinClient binClient;

    public void sendMail(AssignmentNotif notif) throws MessagingException {
        String location = binClient.getLocationById(notif.getBinId()).getBody();

        notif.setLocation(location);
        mailSenderService.sendEmail(notif);
    }
}
