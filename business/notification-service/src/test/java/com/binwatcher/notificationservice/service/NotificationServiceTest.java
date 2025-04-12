package com.binwatcher.notificationservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.TypeNotif;
import com.binwatcher.notificationservice.client.BinClient;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private BinClient binClient;

    @InjectMocks
    private NotificationService notificationService;

    private AssignmentNotif notif;

    @BeforeEach
    void setUp() {
        notif = new AssignmentNotif();
        notif.setBinId("bin123");
        notif.setEmail("driver@example.com");
        notif.setTypeNotif(TypeNotif.ASSIGNMENT);
    }

    @Test
    void sendMail() throws Exception {
        when(binClient.getLocationById("bin123")).thenReturn(ResponseEntity.ok("Location Example"));

        notificationService.sendMail(notif);

        verify(mailSenderService, times(1)).sendEmail(notif);
        assertEquals("Location Example", notif.getLocation());
    }

    @Test
    void sendMaiFailed() throws MessagingException {
        when(binClient.getLocationById("bin123")).thenReturn(ResponseEntity.ok("Location Example"));
        doThrow(new MessagingException("Mail error")).when(mailSenderService).sendEmail(notif);

        MessagingException exception = assertThrows(MessagingException.class, () -> {
            notificationService.sendMail(notif);
        });

        assertEquals("Mail error", exception.getMessage());
    }
}
