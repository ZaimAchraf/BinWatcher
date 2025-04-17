package com.binwatcher.notificationservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.TypeNotif;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${email.from}")
    private String from;

    private final String BIN_ASSIGNMENT_SUBJECT = "You Have Been Assigned a New Bin";
    private final String BIN_UNASSIGNMENT_SUBJECT = "Bin Unassignment Notification";


    public void sendEmail(AssignmentNotif notification) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("action", notification.getTypeNotif().equals(TypeNotif.ASSIGNMENT) ? "Bin Assignment" : "Bin Unassignment");
        variables.put("binId", notification.getBinId());
        variables.put("location", notification.getLocation());
        context.setVariables(variables);
        String htmlContent = templateEngine.process("notif-template", context);

        helper.setFrom(from);
        helper.setTo(notification.getEmail());
        helper.setSubject(
                notification.getTypeNotif().equals(TypeNotif.ASSIGNMENT)
                        ? BIN_ASSIGNMENT_SUBJECT
                        : BIN_UNASSIGNMENT_SUBJECT
        );        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
