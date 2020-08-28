package com.epam.ms.queue;

import com.epam.ms.event.Event;
import com.epam.ms.service.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueHandler {
    private static final String USER_EVENTS_QUEUE = "userEventsQueue";
    private static final String GROUP = "nutrition";

    @NonNull
    private AmqpTemplate template;
    @NonNull
    private ObjectMapper jacksonMapper;

    public void sendEventToQueue(String id, String eventKey) {
        log.info("Send event {} to queue {}", eventKey, USER_EVENTS_QUEUE);
        Event event = new Event(GROUP, eventKey, Map.of("id", id));
        try {
            template.convertAndSend(USER_EVENTS_QUEUE, jacksonMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new ServiceException(String.format("Event %s could not be serialized", event), e);
        }
    }
}
