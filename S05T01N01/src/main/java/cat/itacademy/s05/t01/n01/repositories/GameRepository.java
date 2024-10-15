package cat.itacademy.s05.t01.n01.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t01.n01.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, Integer>{

}
