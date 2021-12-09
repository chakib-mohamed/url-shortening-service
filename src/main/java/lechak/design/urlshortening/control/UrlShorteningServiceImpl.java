package lechak.design.urlshortening.control;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import lechak.design.urlshortening.boundary.CreateShortURLCommand;
import lechak.design.urlshortening.control.exception.URLNotFoundException;
import lechak.design.urlshortening.control.policies.Policy;
import lechak.design.urlshortening.entity.Url;

/**
 * @author chakib mohamed
 */
@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    final UrlRepository urlRepository;
    final Policy<String> uniqueAliasPolicy;
    final Policy<String> uniqueURLPolicy;

    public UrlShorteningServiceImpl(UrlRepository urlRepository,
            @Qualifier("UniqueAliasPolicy") Policy<String> uniqueAliasPolicy,
            @Qualifier("UniqURLPolicy") Policy<String> uniqueURLPolicy) {
        this.urlRepository = urlRepository;
        this.uniqueAliasPolicy = uniqueAliasPolicy;
        this.uniqueURLPolicy = uniqueURLPolicy;
    }


    @Override
    public Url getOriginalURL(String hash) {

        return urlRepository.findById(hash)
                .orElseThrow(() -> new URLNotFoundException("URL not found"));
    }

    @Override
    public Url registerURL(CreateShortURLCommand createShortURLCommand) {

        this.uniqueAliasPolicy.validate(createShortURLCommand.getCustomAlia());
        var hash = Optional.ofNullable(createShortURLCommand.getCustomAlia())
                .orElse(shortenURL(createShortURLCommand.getOriginalURL()));
        this.uniqueURLPolicy.validate(hash);

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
        return Url.builder().hash(hash).originalURL(createShortURLCommand.getOriginalURL())
                .creationDate(LocalDateTime.now())
                .expirationDate(Optional.ofNullable(createShortURLCommand.getExpireDate())
                        .orElse(LocalDateTime.now().plusMonths(1)))
                .build();
    }

}
