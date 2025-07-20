package org.example.entities.strategy;

import org.example.entities.ResetStrategy;

public class CarryForwardReset implements ResetStrategy {
  @Override
  public int resetTokens(int tokens, int currentTokens) {
    int maxTokens = 2 * tokens;
    if (currentTokens > 0) {
      tokens += currentTokens;
    }
    return Math.min(tokens, maxTokens);
  }
}
