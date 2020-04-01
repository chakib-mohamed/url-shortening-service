package design.urlshortening.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

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
