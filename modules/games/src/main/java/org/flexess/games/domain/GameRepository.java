package org.flexess.games.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Games.
 * Via Spring Data JPA, see https://projects.spring.io/spring-data-jpa/
 *
 * @author stefanz
 */
public interface GameRepository extends CrudRepository<Game, Long> {
}
