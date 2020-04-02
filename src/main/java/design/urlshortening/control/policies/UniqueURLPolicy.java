package design.urlshortening.control.policies;

import design.urlshortening.control.UrlRepository;
import design.urlshortening.control.exception.URLAlreadyRegistredException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Qualifier("UniqURLPolicy")
public class UniqueURLPolicy implements Policy<String> {

    final UrlRepository urlRepository;

    public UniqueURLPolicy(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public void validate(String hash) {
       if(!StringUtils.isEmpty(hash) && urlRepository.findById(hash).isPresent()){
           throw new URLAlreadyRegistredException("URL already registered");
       }
    }
}
