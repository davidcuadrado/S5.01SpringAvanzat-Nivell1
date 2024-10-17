package cat.itacademy.s05.t01.n01.services;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PlayerService {
	
	@Autowired
	PlayerRepository playerRepository;
	
	@Transactional(readOnly = true)
	public Flux<Player> getAllPlayersByRanking() {
		return playerRepository.findAll()
				.sort(Comparator.comparing(Player::getPlayerMaxPoints).thenComparing(Player::getGamesPlayed));

	}
	
	@Transactional
	public Mono<Player> changePlayerName(int playerId, String inputPlayerName){
		return playerRepository.findById(playerId)
				.flatMap(player -> {
					player.setPlayerName(inputPlayerName);
					return playerRepository.save(player);
				})
				
				;
	}

}
