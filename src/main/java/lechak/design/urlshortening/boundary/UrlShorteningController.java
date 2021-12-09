package lechak.design.urlshortening.boundary;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import lechak.design.urlshortening.control.UrlShorteningService;
import lechak.design.urlshortening.entity.Url;

@RestController
public class UrlShorteningController {

    private final UrlShorteningService urlShorteningService;

    public UrlShorteningController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping("/short-url")
    public ResponseEntity<Url> createURL(@RequestBody CreateShortURLCommand createShortURLCommand,
            UriComponentsBuilder uriComponentsBuilder) {

        var url = urlShorteningService.registerURL(createShortURLCommand);

        return ResponseEntity.created(uriComponentsBuilder.path("/short-url/{hash}")
                .buildAndExpand(url.getHash()).toUri()).body(url);
    }

    @GetMapping("/short-url/{hash}")
    public Url getShortURL(@PathVariable String hash) {

        return urlShorteningService.getOriginalURL(hash);

    }

    @GetMapping("/{hash}")
    public void redirectToOriginalURL(@PathVariable String hash, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(urlShorteningService.getOriginalURL(hash).getOriginalURL());
    }
}
