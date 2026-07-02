package org.example.ratelimiter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {
    @Test
    void 용량만큼_허용되고_초과하면_거절된다() {
        FakeCurrentTime clock = new FakeCurrentTime(0);
        TokenBucketRateLimiter limiter = new
                TokenBucketRateLimiter(5, 2.0, clock);

        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire());
        }
        assertFalse(limiter.tryAcquire());
    }

    @Test
    void 시간이_흐르면_리필된_만큼_다시_허용된다() {
        FakeCurrentTime clock = new FakeCurrentTime(0);
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 2.0, clock);
        for (int i = 0; i < 5; i++) limiter.tryAcquire();

        clock.advance(1000); // 1초 경과 → 2개 리필

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
    }
}