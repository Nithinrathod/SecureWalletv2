package com.tcs.notificationService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tcs.notificationService.bean.Notification;
import com.tcs.notificationService.dto.NotificationDto;
import com.tcs.notificationService.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationDto sendInApp(NotificationDto dto) {

        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setType("INAPP");
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());

        notificationRepository.save(notification);

        // In-App (DB based)
        dto.setStatus("SUCCESS");
        return dto;
    }

    public NotificationDto sendEmail(NotificationDto dto) {

        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setType("EMAIL");
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());

        notificationRepository.save(notification);

        // 🔥 REAL EMAIL SENDING
        emailService.sendEmail(
                dto.getUserId(),   // assuming userId = email
                dto.getTitle(),
                dto.getMessage()
        );

        dto.setStatus("SUCCESS");
        return dto;
    }
}

