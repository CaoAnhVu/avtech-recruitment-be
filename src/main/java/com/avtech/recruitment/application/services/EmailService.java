package com.avtech.recruitment.application.services;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.avtech.recruitment.domain.recruitment.Interview;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendInterviewInvitation(Interview interview, String candidateEmail, String candidateName) {
        log.info("Generating ICS and sending email to {}", candidateEmail);

        try {
            ICalendar ical = new ICalendar();
            VEvent event = new VEvent();

            event.setSummary("Interview Invitation: AVTech Recruitment");
            event.setDateStart(Date.from(interview.getStartTime()));
            event.setDateEnd(Date.from(interview.getEndTime()));
            
            String location = interview.getLocationType().name();
            if (interview.getMeetingLink() != null) {
                location += " - " + interview.getMeetingLink();
            }
            event.setLocation(location);
            event.setDescription("You have been invited to an interview. Location/Link: " + location);

            ical.addEvent(event);
            String icalContent = Biweekly.write(ical).go();

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(candidateEmail);
            helper.setSubject("Interview Invitation");
            helper.setText("Dear " + candidateName + ",\n\nPlease find your interview invitation attached.\n\nBest Regards,\nAVTech Recruitment Team");

            helper.addAttachment("invite.ics", new ByteArrayResource(icalContent.getBytes()));

            emailSender.send(message);
            log.info("Email sent successfully to {}", candidateEmail);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", candidateEmail, e);
        }
    }
}
