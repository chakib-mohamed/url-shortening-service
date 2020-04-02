package design.urlshortening.control.policies;

import design.urlshortening.control.UrlRepository;
import design.urlshortening.control.exception.AliasAlreadyUsedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Qualifier("UniqueAliasPolicy")
public class UniqueAliasPolicy implements Policy<String> {

    final UrlRepository urlRepository;

    public UniqueAliasPolicy(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public void validate(String alias) {
       if(!StringUtils.isEmpty(alias) && urlRepository.findById(alias).isPresent()){
           throw new AliasAlreadyUsedException("Alias already used");
       }
    }
}
