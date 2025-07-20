package org.example.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.example.entities.Bucket;
import org.example.entities.ResetStrategy;
import org.example.entities.User;
import org.example.entities.strategy.CarryForwardReset;
import org.example.exceptions.UserNotFoundException;

public class RateLimiterService {
  private static final RateLimiterService rateLimiterService = new RateLimiterService();
  private final Map<Long, User> userMap = new ConcurrentHashMap<>();

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  ;

  private RateLimiterService() {
    scheduler.scheduleAtFixedRate(this::replenishTokens, 0, 1, TimeUnit.SECONDS);
  }

  public static RateLimiterService getInstance() {
    return rateLimiterService;
  }

  public void registerUser(long id, String name, int initialTokens) {
    ResetStrategy resetStrategy = new CarryForwardReset();
    User user =
        new User().setId(id).setName(name).setBucket(new Bucket(initialTokens, resetStrategy));
    userMap.put(id, user);
    System.out.println("User " + name + " registered");
  }

  public User getUser(long id) {
    if (!userMap.containsKey(id)) {
      throw new UserNotFoundException("User not found for id :" + id);
    }
    return userMap.get(id);
  }

  private void replenishTokens() {
    userMap.values().forEach(user -> user.creditUser(1)); // refill 1 token every second
  }

  public void shutdown() {
    scheduler.shutdown();
  }
}
