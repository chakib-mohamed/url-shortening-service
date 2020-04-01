package design.urlshortening.control;

import design.urlshortening.boundary.CreateShortURLCommand;
import lombok.Builder;
import lombok.Data;

import java.util.function.Predicate;

@Data
@Builder
public class Policy {

    private Predicate<CreateShortURLCommand> rule;
    private String message;
}
