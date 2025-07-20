import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.services.RateLimiterService;
import org.junit.jupiter.api.Test;

public class RateLimiterServiceTests {

  RateLimiterService rateLimiterService = RateLimiterService.getInstance();

  @Test
  public void testRegisterUser() {
    rateLimiterService.registerUser(1L, "Aman", 2);
    assertEquals("Aman", rateLimiterService.getUser(1L).getName());

    assertEquals(2, rateLimiterService.getUser(1L).getBucket().getTokens());
  }

  @Test
  public void testReplenishTokens() throws InterruptedException {

    rateLimiterService.registerUser(1L, "Aman", 1);

    rateLimiterService.getUser(1).hitApi();

    assertEquals(0, rateLimiterService.getUser(1L).getBucket().getTokens());

    Thread.sleep(1100);

    assertEquals(1, rateLimiterService.getUser(1L).getBucket().getTokens());

    Thread.sleep(1100);

    assertEquals(2, rateLimiterService.getUser(1L).getBucket().getTokens());
  }
}
