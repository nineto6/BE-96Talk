package nineto6.Talk.global.chat.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubMemberRedisRepository extends CrudRepository<SubMember, Long> {
    Optional<SubMember> findBySessionId(String sessionId);
}
