package lechak.design.urlshortening;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import lechak.design.urlshortening.boundary.CreateShortURLCommand;
import lechak.design.urlshortening.entity.Url;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UrlShorteningApplicationTests {

    @Container
    public static GenericContainer redis =
            new GenericContainer(DockerImageName.parse("redis:6.2.6-alpine"))
                    .withExposedPorts(6379);


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.server.host", redis::getHost);
        registry.add("redis.server.port", redis::getFirstMappedPort);
    }

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Test
    void createANewShortURLWithCustomAliaShouldReturn201() {

        var testPayload = CreateShortURLCommand.builder().originalURL("https://test.com")
                .customAlia("alia").build();
        var response = testRestTemplate.postForEntity("/short-url", testPayload, Url.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody().getHash(), testPayload.getCustomAlia());
    }

    @Test
    void redierctToOriginalURLGivenHashShouldWork() {

        var testPayload = CreateShortURLCommand.builder().originalURL("https://test11.com").build();
        var response = testRestTemplate.postForEntity("/short-url", testPayload, Url.class);

        var hash = response.getBody().getHash();

        var getURLResponse = testRestTemplate.getForEntity("/short-url/{hash}", Url.class, hash);
        var redierctToResponse = testRestTemplate.getForEntity("/{hash}", Url.class, hash);

        assertEquals(getURLResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(getURLResponse.getBody().getHash(), hash);
        assertEquals(redierctToResponse.getStatusCode(), HttpStatus.FOUND);

    }

    @Test
    void getUnexistingURLGivenHashShouldReturn406() {

        var getURLResponse =
                testRestTemplate.getForEntity("/short-url/{hash}", Url.class, "random");

        assertEquals(getURLResponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void redierctToAnExpiredURLShouldReturn406() {

        var testPayload = CreateShortURLCommand.builder().originalURL("https://test3.com")
                .expireDate(LocalDateTime.now()).build();
        var response = testRestTemplate.postForEntity("/short-url", testPayload, Url.class);

        var getURLResponse = testRestTemplate.getForEntity("/short-url/{hash}", Url.class,
                response.getBody().getHash());

        assertEquals(getURLResponse.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);

    }

    @Test
    void createANewShortURLWithAnExistingAliaShouldReturn406() {
        var testPayload = CreateShortURLCommand.builder().originalURL("https://test1.com")
                .customAlia("alia1").build();

        testRestTemplate.postForEntity("/short-url", testPayload, Url.class);
        var response = testRestTemplate.postForEntity("/short-url", testPayload, Url.class);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    void createANewShortURLForAnExistingURLShouldReturn406() {
        var testPayload = CreateShortURLCommand.builder().originalURL("https://test2.com").build();

        testRestTemplate.postForEntity("/short-url", testPayload, Url.class);
        var response = testRestTemplate.postForEntity("/short-url", testPayload, Url.class);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
    }

}
