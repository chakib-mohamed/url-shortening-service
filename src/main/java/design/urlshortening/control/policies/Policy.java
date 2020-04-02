package design.urlshortening.control.policies;

public interface Policy<T> {

    void validate(T t);

}
