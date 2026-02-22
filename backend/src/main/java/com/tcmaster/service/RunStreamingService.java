package com.tcmaster.service;

import com.tcmaster.dto.RunDtos;
import com.tcmaster.enums.TestRunStatus;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RunStreamingService {

    private final SimpMessagingTemplate messagingTemplate;

    public RunStreamingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publish(Long runId, String type, String message, TestRunStatus status) {
        RunDtos.RunEvent event = new RunDtos.RunEvent(runId, type, message, status, LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/runs/" + runId, event);
    }
}
