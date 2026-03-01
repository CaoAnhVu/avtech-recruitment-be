package com.avtech.recruitment.application.events;

import com.avtech.recruitment.domain.recruitment.Interview;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InterviewScheduledEvent extends ApplicationEvent {
    private final Interview interview;

    public InterviewScheduledEvent(Object source, Interview interview) {
        super(source);
        this.interview = interview;
    }
}
