package design.urlshortening.control;

import design.urlshortening.boundary.CreateShortURLCommand;
import design.urlshortening.control.exception.BusinessException;
import design.urlshortening.control.exception.URLNotFoundException;
import design.urlshortening.entity.Url;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * @author chakib mohamed
 */
@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    final UrlRepository urlRepository;

    public UrlShorteningServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Business Rules
    List<Policy> policies = new ArrayList<>();

    @PostConstruct
    private void initBusinessRules() {
//        policies.add(
//                Policy.builder().rule(command -> command.getOriginalURL().startsWith("https://example/"))
//                                .message("URL must start with example").build()
//        );
        policies.add(
                Policy.builder().rule(command -> StringUtils.isEmpty(command.getCustomAlia()) || urlRepository.findById(command.getCustomAlia()).isEmpty())
                        .message("Alias is already used").build()
        );
    }


    @Override
    public Url getOriginalURL(String hash) throws BusinessException {

        return urlRepository.findById(hash).orElseThrow(() -> new URLNotFoundException("URL not found"));
    }

    @Override
    public Url registerURL(CreateShortURLCommand createShortURLCommand) throws BusinessException {
        for (Policy policy : policies) {
            if (!policy.getRule().test(createShortURLCommand)) {
                throw new BusinessException(policy.getMessage());
            }
        }

        var hash = Optional.ofNullable(createShortURLCommand.getCustomAlia()).orElse(shortenURL(createShortURLCommand.getOriginalURL()));
        var url = this.createUrlEntity(createShortURLCommand, hash);
        return urlRepository.save(url);


    }

    private String shortenURL(String originalURL) {
        return b64encode(HashUtils.hash(originalURL)).substring(0, 8);
    }

    private String b64encode(String originalInput) {
        return Base64.getEncoder().encodeToString(originalInput.getBytes());
    }

    private Url createUrlEntity(CreateShortURLCommand createShortURLCommand, String hash) {
        return Url.builder().hash(hash)
                .originalURL(createShortURLCommand.getOriginalURL())
                .creationDate(LocalDateTime.now())
                .expirationDate(Optional.ofNullable(createShortURLCommand.getExpireDate()).orElse(LocalDateTime.now().plusMonths(1)))
                .build();
    }

}
