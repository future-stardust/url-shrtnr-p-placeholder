package edu.kpi.testcourse.storage;

import edu.kpi.testcourse.entities.UrlAlias;
import edu.kpi.testcourse.logic.UrlShortenerConfig;
import edu.kpi.testcourse.serialization.JsonToolJacksonImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UrlRepositoryFileImplTest {

  UrlShortenerConfig appConfig;
  UrlRepository urlRepository;

  @BeforeEach
  void setUp() {
    try {
      appConfig = new UrlShortenerConfig(
        Files.createTempDirectory("url-repository-file-test"));
      Files.write(appConfig.storageRoot().resolve("url-repository.json"), "{}".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    urlRepository = new UrlRepositoryFileImpl(new JsonToolJacksonImpl(), appConfig);
  }

  @AfterEach
  void tearDown() {
    try {
      Files.delete(appConfig.storageRoot().resolve("url-repository.json"));
      Files.delete(appConfig.storageRoot());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Test
  void shouldCreateAlias() {
    // WHEN
    UrlAlias alias = new UrlAlias("http://r.com/short", "http://g.com/long", "aaa@bbb.com");
    urlRepository.createUrlAlias(alias);

    // THEN
    assertThat(urlRepository.findUrlAlias("http://r.com/short")).isEqualTo(alias);
  }


}
