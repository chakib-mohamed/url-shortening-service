package design.urlshortening.boundary;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateShortURLCommand {

    private String originalURL;
    private String customAlia;
    private LocalDateTime expireDate;
}
