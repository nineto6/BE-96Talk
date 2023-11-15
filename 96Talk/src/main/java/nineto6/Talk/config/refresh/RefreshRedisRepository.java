package nineto6.Talk.config.refresh;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * CrudRepository 를 상속하는 CustomInterface 를 생성
 * redisRepository 방식은 CrudRepository 를 상속받은 인터페이스가 사용되기 때문에 Spring Data JPA 에서
 * JpaRepository 를 사용하는 방식과 유사하다는 특징이 있다.
 */
public interface RefreshRedisRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByMemberEmail(String memberEmail);
}
