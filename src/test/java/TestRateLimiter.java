import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.example.entities.Bucket;
import org.example.entities.User;
import org.example.exceptions.RateLimitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRateLimiter {
  ScheduledExecutorService scheduler;

  @BeforeEach
  public void init() {
    this.scheduler = Executors.newScheduledThreadPool(1);
  }

  @AfterEach
  public void cleanup() {
    scheduler.shutdownNow();
  }

  @org.junit.jupiter.api.Test
  public void createUserAndCreditToken() {

    Bucket bucket = new Bucket();
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    user.creditUser(100);

    assertEquals(100, user.getBucket().getTokens());
  }

  @org.junit.jupiter.api.Test
  public void createUserAndCreditTokenAndUserToken() {

    Bucket bucket = new Bucket();
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    user.creditUser(100);
    user.hitApi();

    assertEquals(99, user.getBucket().getTokens());
  }

  @org.junit.jupiter.api.Test
  public void userLimitExceed() {

    Bucket bucket = new Bucket();
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    user.creditUser(1);
    user.hitApi();

    assertEquals(0, user.getBucket().getTokens());

    assertThrows(RateLimitException.class, user::hitApi);
  }

  @org.junit.jupiter.api.Test
  public void creditUserPerTimeInterval() throws InterruptedException {

    Bucket bucket = new Bucket();
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    user.creditUser(1);
    user.hitApi();

    assertThrows(RateLimitException.class, user::hitApi);

    scheduler.scheduleAtFixedRate(
        () -> user.creditUser(2), 0, 1, java.util.concurrent.TimeUnit.SECONDS);

    Thread.sleep(100);
    user.hitApi();
    assertEquals(1, user.getBucket().getTokens());
    user.hitApi();
    assertEquals(0, user.getBucket().getTokens());

    assertThrows(RateLimitException.class, user::hitApi);
    Thread.sleep(1000);

    assertEquals(2, user.getBucket().getTokens());

    user.hitApi();
    assertEquals(1, user.getBucket().getTokens());
  }

  @Test
  public void testTokenPropagationAcrossWindow() throws InterruptedException {

    Bucket bucket = new Bucket();
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    scheduler.scheduleAtFixedRate(
        () -> user.creditUser(10), 0, 1, java.util.concurrent.TimeUnit.SECONDS);

    Thread.sleep(100);

    assertEquals(10, user.getBucket().getTokens());

    user.hitApi();
    user.hitApi();

    assertEquals(8, user.getBucket().getTokens());

    Thread.sleep(1000);

    assertEquals(14, user.getBucket().getTokens());
  }
}
