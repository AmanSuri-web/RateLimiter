package org.example.entities.strategy;

import org.example.entities.ResetStrategy;

public class FixedReset implements ResetStrategy {
  @Override
  public int resetTokens(int tokens, int currentTokens) {
    return tokens;
  }
}
