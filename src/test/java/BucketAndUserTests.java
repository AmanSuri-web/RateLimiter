import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.entities.Bucket;
import org.example.entities.User;
import org.example.entities.strategy.CarryForwardReset;
import org.example.exceptions.RateLimitException;
import org.junit.jupiter.api.Test;

public class BucketAndUserTests {

  @org.junit.jupiter.api.Test
  public void createUserAndCreditToken() {

    Bucket bucket = new Bucket(100, new CarryForwardReset());
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    assertEquals(100, user.getBucket().getTokens());
  }

  @org.junit.jupiter.api.Test
  public void createUserAndCreditTokenAndUserToken() {

    Bucket bucket = new Bucket(100, new CarryForwardReset());
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    user.hitApi();

    assertEquals(99, user.getBucket().getTokens());
  }

  @org.junit.jupiter.api.Test
  public void userLimitExceed() {

    Bucket bucket = new Bucket(1, new CarryForwardReset());
    User user = new User().setId(1L).setName("John Doe").setBucket(bucket);

    user.hitApi();

    assertEquals(0, user.getBucket().getTokens());

    assertThrows(RateLimitException.class, user::hitApi);
  }

  @Test
  public void testTokenCarryForward() {

    Bucket bucket = new Bucket(1, new CarryForwardReset());

    bucket.resetTokens(1);

    assertEquals(2, bucket.getTokens());

    bucket.resetTokens(1);

    assertEquals(2, bucket.getTokens());
  }
}
