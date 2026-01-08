package com.tcs.notificationService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.tcs.notificationService.dto.NotificationDto;
import com.tcs.notificationService.service.NotificationService;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

 // In-App Notification
    @PostMapping("/inapp")
    public NotificationDto sendInApp(@RequestBody NotificationDto dto) {
        dto.setType("INAPP");
        return notificationService.sendInApp(dto);
    }

    // Email Notification
    @PostMapping("/email")
    public NotificationDto sendEmail(@RequestBody NotificationDto dto) {
        dto.setType("EMAIL");
        return notificationService.sendEmail(dto);
    }
}
