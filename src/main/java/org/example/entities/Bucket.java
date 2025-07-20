package org.example.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class Bucket {
  private final AtomicInteger tokens;

  private final ResetStrategy resetStrategy;

  public Bucket(int tokens, ResetStrategy resetStrategy) {
    this.tokens = new AtomicInteger(tokens);
    this.resetStrategy = resetStrategy;
  }

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
    this.tokens.set(resetStrategy.resetTokens(tokens, this.tokens.get()));
  }
}
