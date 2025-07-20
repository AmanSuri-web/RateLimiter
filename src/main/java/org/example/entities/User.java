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
      throw new RateLimitException("Rate limit exceeded for user: " + name);
    } else {
      System.out.println("Token used by user: " + name);
    }
  }

  public void creditUser(int tokens) {
    System.out.println(tokens + " tokens credited for user: " + name);
    bucket.resetTokens(tokens);
  }
}
