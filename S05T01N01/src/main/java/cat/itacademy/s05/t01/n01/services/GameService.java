package cat.itacademy.s05.t01.n01.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import reactor.core.publisher.Mono;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Transactional
	public Game createNewGame(Player player) {
		String gameId = generateGameId(player.getPlayerId());
		
		Game newGame = new Game(player.getPlayerName(), gameId);
		return gameRepository.save(newGame);
		
	}
	
	private String generateGameId(int playerId) {
		return playerId + "-" + System.currentTimeMillis();
	}
	
	
	@Transactional(readOnly = true)
	public Mono<Game> getGameById(String gameId) {
		return Mono.justOrEmpty(gameRepository.findById(gameId))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));
		
	}
	

}
