package com.binwatcher.notificationservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.TypeNotif;
import com.binwatcher.notificationservice.client.BinClient;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    private final MailSenderService mailSenderService;
    private final BinClient binClient;
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    public void sendMail(AssignmentNotif notif) throws MessagingException {
        String location = binClient.getLocationById(notif.getBinId()).getBody();
        notif.setLocation(location);
        LOG.info("Sending {} mail to driver with id {}.",
                notif.getTypeNotif().equals(TypeNotif.ASSIGNMENT) ? "assignment" : "unassignment",
                notif.getEmail());
        mailSenderService.sendEmail(notif);
    }
}
