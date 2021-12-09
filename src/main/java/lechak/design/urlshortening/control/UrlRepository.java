package lechak.design.urlshortening.control;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import lechak.design.urlshortening.entity.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, String> {
}
