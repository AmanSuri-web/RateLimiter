package org.example.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class Bucket {
  private AtomicInteger tokens = new AtomicInteger(0);

  public boolean useToken() {
    if (tokens.get() > 0) {
      tokens.decrementAndGet();
      return true;
    }
    return false;
  }

  public int getTokens() {
    return tokens.get();
  }

  public void resetTokens(int tokens) {
    if (tokens <= 0) {
      throw new IllegalArgumentException("Tokens must be greater than zero");
    }
    int maxTokens = 2 * tokens;
    if (this.tokens.get() > 0) {
      tokens += this.tokens.get() / 2; // Add half of the current tokens to the new value
    }
    tokens = Math.min(tokens, maxTokens);
    this.tokens.set(tokens);
  }
}
