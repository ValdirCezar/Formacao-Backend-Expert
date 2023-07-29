package br.com.valdircezar.authserviceapi.repositories;

import br.com.valdircezar.authserviceapi.models.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
}
