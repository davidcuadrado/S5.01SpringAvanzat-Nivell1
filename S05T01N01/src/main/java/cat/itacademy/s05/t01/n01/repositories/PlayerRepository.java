package cat.itacademy.s05.t01.n01.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import cat.itacademy.s05.t01.n01.model.Player;

@Repository
public interface PlayerRepository extends R2dbcRepository<Player, Integer> {
	

}
