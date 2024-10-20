package cat.itacademy.s05.t01.n01.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t01.n01.models.Game;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String>{

}
