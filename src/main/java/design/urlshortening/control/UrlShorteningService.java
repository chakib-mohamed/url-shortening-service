package design.urlshortening.control;

import design.urlshortening.boundary.CreateShortURLCommand;
import design.urlshortening.control.exception.BusinessException;
import design.urlshortening.entity.Url;

public interface UrlShorteningService {

    Url getOriginalURL(String hash) throws BusinessException;

    Url registerURL(CreateShortURLCommand createShortURLCommand) throws BusinessException;
}
