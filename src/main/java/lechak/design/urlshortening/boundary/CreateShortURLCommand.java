package lechak.design.urlshortening.boundary;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateShortURLCommand {

    private String originalURL;
    private String customAlia;
    private LocalDateTime expireDate;
}
