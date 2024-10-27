package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.exceptions.NotFoundException;
import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GameServiceTest {

	@InjectMocks
	private GameService gameService;

	@Mock
	private GameRepository gameRepository;

	@Mock
	private PlayerService playerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createNewGame_ShouldReturnSavedGame() {
		Player player = new Player();
		Game game = new Game(player);

		when(gameRepository.save(any())).thenReturn(Mono.just(game));

		Mono<Game> result = gameService.createNewGame(Mono.just(player));

		StepVerifier.create(result).expectNextMatches(savedGame -> savedGame.getPlayer() != null).verifyComplete();
	}

	@Test
	void getGameById_ShouldReturnGame() {
		String gameId = "1";
		Game game = new Game(new Player("testPlayer"));
		game.setGameId(gameId);

		when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));

		Mono<Game> result = gameService.getGameById(Mono.just(gameId));

		StepVerifier.create(result).expectNextMatches(foundGame -> foundGame.getGameId().equals(gameId))
				.verifyComplete();
	}

	@Test
	void getGameById_ShouldReturnNotFoundException() {
		String gameId = "1";

		when(gameRepository.findById(gameId)).thenReturn(Mono.empty());

		Mono<Game> result = gameService.getGameById(Mono.just(gameId));

		StepVerifier.create(result).expectError(NotFoundException.class).verify();
	}

	@Test
	void nextPlayType_ShouldReturnUpdatedGame() {
		String gameId = "1";
		String playType = "hit";
		Game game = new Game(new Player("testPlayer"));
		game.setIsRunning(true);

		when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));
		when(gameRepository.save(any())).thenReturn(Mono.just(game));

		Mono<Game> result = gameService.nextPlayType(Mono.just(gameId), Mono.just(playType));

		StepVerifier.create(result).expectNextMatches(updatedGame -> updatedGame.getIsRunning()).verifyComplete();
	}

	@Test
	void deleteGameById_ShouldReturnDeletedGame() {
		String gameId = "1";
		Game game = new Game(new Player("testPlayer"));
		game.setGameId(gameId);

		when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));
		when(gameRepository.delete(game)).thenReturn(Mono.empty());

		Mono<Game> result = gameService.deleteGameById(Mono.just(gameId));

		StepVerifier.create(result).expectNext(game).verifyComplete();

		when(gameRepository.findById(gameId)).thenReturn(Mono.empty());

		result = gameService.deleteGameById(Mono.just(gameId));

		StepVerifier.create(result).expectError(NotFoundException.class).verify();
	}

}
