package com.binwatcher.notificationservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.FillAlert;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AssignmentNotifConsumer {

    private final NotificationService notificationService;
    private static final Logger LOG = LoggerFactory.getLogger(AssignmentNotifConsumer.class);

    @KafkaListener(
            topics = "assignment-notif",
            groupId = "notification-service-group",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeBinFill(ConsumerRecord<String, AssignmentNotif> record, Acknowledgment acknowledgment) {

        AssignmentNotif assignmentNotif = record.value();
        LOG.info("Received notif => driverId : " + assignmentNotif.getDriverId()
                + ", email : " + assignmentNotif.getEmail()
                + ", binId : " + assignmentNotif.getBinId());

        try {
            notificationService.sendMail(assignmentNotif);
            acknowledgment.acknowledge();
        } catch (MessagingException e) {
            LOG.error("Error sending email: " + e.getMessage());
        }
    }
}
