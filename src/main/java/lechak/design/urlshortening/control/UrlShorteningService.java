package lechak.design.urlshortening.control;

import lechak.design.urlshortening.boundary.CreateShortURLCommand;
import lechak.design.urlshortening.control.exception.BusinessException;
import lechak.design.urlshortening.entity.Url;

public interface UrlShorteningService {

    Url getOriginalURL(String hash) throws BusinessException;

    Url registerURL(CreateShortURLCommand createShortURLCommand) throws BusinessException;
}
