package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.services.GameService;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GameControllerTest {

	@InjectMocks
	private GameController gameController;

	@Mock
	private GameService gameService;

	@Mock
	private PlayerService playerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createNewGame_ShouldReturnCreatedGame() {
		String playerName = "testPlayer";

		when(playerService.createNewPlayer(any())).thenReturn(Mono.just(new Player()));
		when(gameService.createNewGame(any())).thenReturn(Mono.just(new Game(new Player(playerName))));

		Mono<ResponseEntity<Game>> response = gameController.createNewGame(playerName);

		StepVerifier.create(response)
				.expectNextMatches(res -> res.getStatusCode() == HttpStatus.CREATED && res.getBody() != null)
				.verifyComplete();
	}

	@Test
	void getGameDetails_ShouldReturnGameDetails() {
		String gameId = "1";
		Game game = new Game(new Player("testPlayer"));
		game.setGameId(gameId);

		when(gameService.getGameById(any())).thenReturn(Mono.just(game));

		Mono<ResponseEntity<Game>> response = gameController.getGameDetails(gameId);

		StepVerifier.create(response)
				.expectNextMatches(
						res -> res.getStatusCode() == HttpStatus.OK && res.getBody().getGameId().equals(gameId))
				.verifyComplete();
	}

	@Test
	void makePlay_ShouldReturnUpdatedGame() {
		String gameId = "1";
		String playType = "hit";
		Game game = new Game(new Player("testPlayer"));

		when(gameService.nextPlayType(any(), any())).thenReturn(Mono.just(game));

		Mono<ResponseEntity<Game>> response = gameController.makePlay(gameId, playType);

		StepVerifier.create(response)
				.expectNextMatches(res -> res.getStatusCode() == HttpStatus.OK && res.getBody() != null)
				.verifyComplete();
	}

	@Test
	void deleteGame_ShouldReturnNoContent() {
		String gameId = "1";

		when(gameService.deleteGameById(any())).thenReturn(Mono.just(new Game(new Player("testPlayer"))));

		Mono<ResponseEntity<String>> response = gameController.deleteGame(gameId);

		StepVerifier.create(response).expectNextMatches(res -> res.getStatusCode() == HttpStatus.NO_CONTENT)
				.verifyComplete();
	}
}
