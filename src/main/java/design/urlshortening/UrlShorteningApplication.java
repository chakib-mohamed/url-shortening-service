package design.urlshortening;

import design.urlshortening.boundary.CreateShortURLCommand;
import design.urlshortening.control.exception.BusinessException;
import design.urlshortening.control.UrlShorteningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class UrlShorteningApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShorteningApplication.class, args);
    }

    public UrlShorteningApplication(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    private final UrlShorteningService urlShorteningService;

    @EventListener(ApplicationReadyEvent.class)
    public void onAfterStartUp(){
        try {
            urlShorteningService.registerURL(CreateShortURLCommand.builder().originalURL("https://example/v=test").customAlia("expl").expireDate(LocalDateTime.now().plusMonths(1)).build());
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
    }
}
