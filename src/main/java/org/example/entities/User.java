package org.example.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.exceptions.RateLimitException;

@Data
@Accessors(chain = true)
public class User {
  private long id;
  private String name;
  private Bucket bucket;

  public void hitApi() {
    boolean tokenUsed = bucket.useToken();

    if (!tokenUsed) {
      System.out.println("Rate limit exceeded for user: " + name);
      throw new RateLimitException("Rate limit exceeded for user: " + name);
    }
  }

  public void creditUser(int tokens) {
    bucket.resetTokens(tokens);
  }
}
