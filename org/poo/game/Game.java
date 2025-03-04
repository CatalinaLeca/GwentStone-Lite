package org.poo.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.CardInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;
import org.poo.player.Player;

import java.util.ArrayList;

import static org.poo.cards.Card.convertCardInputToCard;

/**
 * Manages the main game logic, including setup, round progression, and handling actions.
 */
public class Game {

    private static Input inputData;
    private static ArrayNode outputData;

    private static Player player1;
    private static Player player2;
    private static Player currentPlayer;
    private static Player oponentPlayer;

    @Getter
    private static int playerOneWins = Constants.ZERO;
    @Getter
    private static int playerTwoWins = Constants.ZERO;

    @Getter
    private static int round = Constants.ZERO;

    private GameInput currentSession;

    /**
     * Increments the round counter.
     */
    public static void incrementRound() {
        round++;
    }

    /**
     * Increments the win counter for Player One.
     */
    public static void incrementPlayerOneWins() {
        playerOneWins++;
    }

    /**
     * Increments the win counter for Player Two.
     */
    public static void incrementPlayerTwoWins() {
        playerTwoWins++;
    }

    /**
     * Starts a new round by incrementing mana, adding cards to hands, and resetting card attacks.
     */
    public static void startNewRound() {
        player1.getManaManager().incrementMana();
        player2.getManaManager().incrementMana();

        player1.addCardInHand();
        player2.addCardInHand();

        player1.resetCardAttacks();
        player2.resetCardAttacks();
    }

    /**
     * Constructs a new Game instance and sets up players and the game state.
     *
     * @param inputData  the game input data
     * @param outputData the output data container
     */
    public Game(final Input inputData, final ArrayNode outputData) {
        Game.inputData = inputData;
        Game.outputData = outputData;

        player1 = playerSetup(1, inputData);
        player2 = playerSetup(2, inputData);

        generateGame();
    }

    /**
     * Sets up a player with their decks and hero based on input data.
     *
     * @param playerIndex the index of the player (1 or 2)
     * @param inputData   the input data for the game
     * @return a Player object initialized with decks and hero
     */
    public static Player playerSetup(final int playerIndex, final Input inputData) {
        Player player = new Player();
        ArrayList<ArrayList<CardInput>> playerDecks;

        if (playerIndex == 1) {
            playerDecks = inputData.getPlayerOneDecks().getDecks();
        } else {
            playerDecks = inputData.getPlayerTwoDecks().getDecks();
        }

        player.getDeckManager().setConvertedDecks(playerDecks);

        return player;
    }

    /**
     * Sets the win count for Player One.
     *
     * @param playerOneWins the number of wins for Player One
     */
    public static void setPlayerOneWins(final int playerOneWins) {
        Game.playerOneWins = playerOneWins;
    }

    /**
     * Sets the win count for Player Two.
     *
     * @param playerTwoWins the number of wins for Player Two
     */
    public static void setPlayerTwoWins(final int playerTwoWins) {
        Game.playerTwoWins = playerTwoWins;
    }

    /**
     * Generates the output data for the game.
     *
     * @return the generated output as an ArrayNode
     */
    public ArrayNode generateOutput() {
        return outputData;
    }

    /**
     * Creates a JSON representation of a card.
     *
     * @param card the card to convert to a JSON node
     * @return an ObjectNode representing the card
     */
    public static ObjectNode getCardNode(final Card card) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode cardNode = objectMapper.createObjectNode();

        int cardType = Card.getCardType(card.getName());

        if (cardType == Constants.ONE) {
            cardNode.put("health", card.getHealth());
            cardNode.put("attackDamage", card.getAttackDamage());
        }

        if (cardType == Constants.TWO) {
            cardNode.put("health", card.getHealth());
        }

        cardNode.put("mana", card.getMana());
        cardNode.put("description", card.getDescription());
        cardNode.put("name", card.getName());

        ArrayNode colors = objectMapper.createArrayNode();
        for (String color : card.getColors()) {
            colors.add(color);
        }

        cardNode.set("colors", colors);

        return cardNode;
    }

    /**
     * Creates an array of JSON nodes for a list of cards.
     *
     * @param cards the list of cards to convert
     * @return an ArrayNode representing the cards
     */
    public static ArrayNode createCardsArrayNode(final ArrayList<Minion> cards) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode cardList = objectMapper.createArrayNode();

        for (Card card : cards) {
            ObjectNode node = getCardNode(card);
            cardList.add(node);
        }

        return cardList;
    }

    /**
     * Initializes the game by setting up players and executing actions.
     */
    private void generateGame() {
        for (GameInput session : inputData.getGames()) {
            currentSession = session;

            player1 = playerSetup(1, inputData);
            player2 = playerSetup(2, inputData);

            gameSetUp(player1);
            gameSetUp(player2);

            currentPlayer = (session.getStartGame().getStartingPlayer() == 1) ? player1 : player2;
            oponentPlayer = (session.getStartGame().getStartingPlayer() == 1) ? player2 : player1;

            for (ActionsInput action : session.getActions()) {
                int commandIndex = Commands.getCommandIndex(action.getCommand());
                getCommand(commandIndex, action);
            }
        }
    }

    /**
     * Sets up the player's deck, hero, and initial state for the game.
     *
     * @param player the player to set up
     */
    private void gameSetUp(final Player player) {
        int playerOneIndex = currentSession.getStartGame().getPlayerOneDeckIdx();
        int playerTwoIndex = currentSession.getStartGame().getPlayerTwoDeckIdx();

        Hero playerOneHero = (Hero) convertCardInputToCard(currentSession.getStartGame()
                .getPlayerOneHero());
        Hero playerTwoHero = (Hero) convertCardInputToCard(currentSession.getStartGame()
                .getPlayerTwoHero());

        if (player.equals(player1)) {
            if (playerOneIndex >= 0 && playerOneIndex < player.getDeckManager().
                    getAllDecks().size()) {
                player.getDeckManager().setCurrentDeckIndex(playerOneIndex);
            }
        } else {
            if (playerTwoIndex >= 0 && playerTwoIndex
                    < player.getDeckManager().getAllDecks().size()) {
                player.getDeckManager().setCurrentDeckIndex(playerTwoIndex);
            }
        }

        player.getDeckManager().replaceCurrentDeck(player.getDeckManager().getAllDecks()
                .get(player.getDeckManager().getCurrentDeckIndex()));
        player.getDeckManager().shuffleDeck(currentSession.getStartGame()
                .getShuffleSeed() + "");

        player.resetCards();
        player.addCardInHand();

        if (player.equals(player1)) {
            player.setHero(playerOneHero);
        } else {
            player.setHero(playerTwoHero);
        }

        player.getManaManager().resetMana();
    }

    /**
     * Executes a command based on its index.
     *
     * @param index  the index of the command
     * @param action the action input associated with the command
     */
    private void getCommand(final int index, final ActionsInput action) {
        Player player = (action.getPlayerIdx() == Constants.ONE) ? player1 : player2;

        if (index > Constants.SIX) {
            ObjectNode actionOutput = JsonNodeFactory.instance.objectNode();
            outputData.add(actionOutput);
            actionOutput.put("command", action.getCommand());

            switch (index) {
                case Constants.SEVEN -> Commands.getCardsInHand(actionOutput, action, player);
                case Constants.EIGHT -> Commands.getPlayerDeck(actionOutput, action, player);
                case Constants.NINE -> Commands.getCardsOnTable(actionOutput, player1, player2);
                case Constants.TEN -> Commands.getPlayerTurn(actionOutput, currentPlayer, player1);
                case Constants.ELEVEN -> Commands.getPlayerHero(actionOutput, action, player);
                case Constants.TWELVE -> Commands.getCardAtPosition(actionOutput,
                        action, player1, player2);
                case Constants.THIRTEEN -> Commands.getPlayerMana(actionOutput, action, player);
                case Constants.FOURTEEN -> Commands.getFrozenCardsOnTable(actionOutput, player1,
                        player2);
                case Constants.FIFTEEN -> Commands.getTotalGamesPlayed(actionOutput);
                case Constants.SIXTEEN -> Commands.getPlayerOneWins(actionOutput);
                case Constants.SEVENTEEN -> Commands.getPlayerTwoWins(actionOutput);
                default -> {
                }
            }
        }

        switch (index) {
            case Constants.ONE -> {
                Commands.endPlayerTurn(currentPlayer, oponentPlayer);
                Player tmp = currentPlayer;
                currentPlayer = oponentPlayer;
                oponentPlayer = tmp;
            }
            case Constants.TWO -> Commands.placeCard(outputData, action, currentPlayer,
                    action.getHandIdx());
            case Constants.THREE -> Commands.cardUsesAttack(outputData, action, player1, player2);
            case Constants.FOUR -> Commands.cardUsesAbility(outputData, action, player1, player2);
            case Constants.FIVE -> Commands.useAttackHero(outputData, action, player1, player2);
            case Constants.SIX -> Commands.useHeroAbility(outputData, action, currentPlayer,
                    player1, player2);
            default -> {
            }
        }
    }
}
