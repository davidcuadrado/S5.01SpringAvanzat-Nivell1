package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;

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
	
	private String generateGameId(String playerId) {
		return playerId + "-" + System.currentTimeMillis();
	}
	

}
