package org.example.distributedlock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Objects;

public class RedisDistributedLock implements DistributedLock {

    private final JedisPool jedisPool;

    public RedisDistributedLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean tryLock(String key, String requestId, long ttlMillis) {
        try (Jedis jedis = jedisPool.getResource()) {
            // TODO: "SET key requestId NX PX ttlMillis" 명령으로 락을 시도하세요.
            //       SetParams.setParams().nx().px(ttlMillis) 를 활용하세요.
            //       - NX: 키가 없을 때만 성공 (이미 락이 걸려있으면 실패)
            //       - PX: TTL(ms) 설정 → 락을 잡은 스레드가 죽어도 자동 해제되도록
            //       jedis.set(key, requestId, params) 결과가 "OK"면 true, null이면 false
            SetParams params = SetParams.setParams().nx().px(ttlMillis);

            return Objects.equals(jedis.set(key, requestId, params), "OK");
        }
    }

    @Override
    public boolean unlock(String key, String requestId) {
        try (Jedis jedis = jedisPool.getResource()) {
            // TODO: "내가 건 락만 내가 푼다"를 보장해야 합니다.
            //       왜 아래처럼 짜면 안전하지 않은지 먼저 생각해보세요:
            //         if (requestId.equals(jedis.get(key))) { jedis.del(key); }
            //       (힌트: GET과 DEL 사이에 다른 스레드/프로세스가 끼어들 여지가 있습니다.)
            //
            //       "값이 requestId와 같을 때만 DEL"을 원자적으로 수행하려면 Lua 스크립트가 필요합니다.
            //       String script =
            //           "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            //           "  return redis.call('del', KEYS[1]) " +
            //           "else return 0 end";
            //       Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
            //       result가 1(Long)이면 true, 0이면 false를 반환하세요.
            String script =
                       "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                       "  return redis.call('del', KEYS[1]) " +
                       "else return 0 end";
            Long result = (Long) jedis.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
            return result == 1;
        }
    }
}
