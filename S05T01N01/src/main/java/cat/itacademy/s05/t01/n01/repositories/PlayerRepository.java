package cat.itacademy.s05.t01.n01.repositories;

import org.springframework.stereotype.Repository;

import cat.itacademy.s05.t01.n01.models.Player;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

@Repository
public interface PlayerRepository extends R2dbcRepository<Player, Integer> {
	

}
