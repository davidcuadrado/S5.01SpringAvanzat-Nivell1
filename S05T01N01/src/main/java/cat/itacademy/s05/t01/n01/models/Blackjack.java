package cat.itacademy.s05.t01.n01.models;

import reactor.core.publisher.Mono;

public class Blackjack {
    private Deck deck;
    private Player player;
    private Player dealer;

    public Blackjack(String playerName) {
        this.deck = new Deck();
        this.player = new Player(playerName); // resulta conflictivo que se pase un String como idfentificador del player
        this.dealer = new Player("Dealer");
    }
    
    
    // revisar métodos en entorno reactivo -> mover a services

    public Mono<Void> dealInitialCards() {
        return Mono.fromRunnable(() -> {
            player.receiveCard(deck.drawCard());
            player.receiveCard(deck.drawCard());
            dealer.receiveCard(deck.drawCard());
        });
    }

    public Mono<String> playerTurn() {
        return Mono.fromCallable(() -> {
            if (player.isBlackjack()) {
                return "Blackjack!";
            } else if (player.isBust()) {
                return "Bust!";
            }
            return "Your turn";
        });
    }

    public Mono<Void> dealerTurn() {
        return Mono.fromRunnable(() -> {
            while (dealer.getScore() < 17) {
                dealer.receiveCard(deck.drawCard());
            }
        });
    }

    public Mono<String> checkWinner() {
        return Mono.fromCallable(() -> {
            if (player.isBust()) {
                return "Dealer wins.";
            } else if (dealer.isBust()) {
                return "Player wins.";
            } else if (player.getScore() > dealer.getScore()) {
                return "Player wins.";
            } else if (player.getScore() < dealer.getScore()) {
                return "Dealer wins.";
            } else {
                return "Draw.";
            }
        });
    }
}
