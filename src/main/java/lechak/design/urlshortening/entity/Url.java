package lechak.design.urlshortening.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash("Url")
public class Url {
    @Id
    private String hash;
    private String originalURL;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
}
