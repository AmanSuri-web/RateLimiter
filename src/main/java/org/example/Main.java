package org.example;

import org.example.services.RateLimiterService;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    System.out.println("Hello, World!");
    RateLimiterService rateLimiterService = RateLimiterService.getInstance();

    rateLimiterService.registerUser(1L, "Alice", 1);
    System.out.println("tokens:" + rateLimiterService.getUser(1L).getBucket().getTokens());
    for (int i = 0; i < 2; i++) {
      try {
        rateLimiterService.getUser(1L).hitApi();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    Thread.sleep(5000);

    for (int i = 0; i < 10; i++) {
      try {
        rateLimiterService.getUser(1L).hitApi();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    rateLimiterService.shutdown();
  }
}
