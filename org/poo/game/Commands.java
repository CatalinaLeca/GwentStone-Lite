package org.poo.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.ActionsInput;
import org.poo.player.Player;

import java.util.ArrayList;
import java.util.Arrays;

import static org.poo.cards.Card.isTank;
import static org.poo.cards.Card.tankExists;
import static org.poo.game.Exceptions.throwException;
import static org.poo.game.Game.createCardsArrayNode;
import static org.poo.game.Game.getCardNode;
import static org.poo.player.Player.getRowIndex;

public final class Commands {

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Commands() { }

    /**
     * Maps a given command string to a predefined constant index.
     *
     * @param command the command string to be mapped
     * @return the corresponding command index as defined in {@link Constants},
     *         or {@code Constants.ZERO} if the command is invalid
     */
    public static int getCommandIndex(final String command) {
        return switch (command) {
            case "endPlayerTurn" -> Constants.ONE;
            case "placeCard" -> Constants.TWO;
            case "cardUsesAttack" -> Constants.THREE;
            case "cardUsesAbility" -> Constants.FOUR;
            case "useAttackHero" -> Constants.FIVE;
            case "useHeroAbility" -> Constants.SIX;

            case "getCardsInHand" -> Constants.SEVEN;
            case "getPlayerDeck" -> Constants.EIGHT;
            case "getCardsOnTable" -> Constants.NINE;
            case "getPlayerTurn" -> Constants.TEN;
            case "getPlayerHero" -> Constants.ELEVEN;
            case "getCardAtPosition" -> Constants.TWELVE;
            case "getPlayerMana" -> Constants.THIRTEEN;
            case "getFrozenCardsOnTable" -> Constants.FOURTEEN;

            case "getTotalGamesPlayed" -> Constants.FIFTEEN;
            case "getPlayerOneWins" -> Constants.SIXTEEN;
            case "getPlayerTwoWins" -> Constants.SEVENTEEN;
            default -> Constants.ZERO;
        };
    }

    /**
     * Retrieves the current deck of the specified player and adds it to the action output.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input
     * @param player       the player whose deck is being retrieved
     */
    public static void getPlayerDeck(final ObjectNode actionOutput, final ActionsInput action,
                                     final Player player) {
        actionOutput.put("playerIdx", action.getPlayerIdx());
        actionOutput.set("output", createCardsArrayNode(player.getDeckManager().getCurrentDeck()));
    }

    /**
     * Retrieves the hero of the specified player and adds it to the action output.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input
     * @param player       the player whose hero is being retrieved
     */
    public static void getPlayerHero(final ObjectNode actionOutput, final ActionsInput action,
                                     final Player player) {
        ObjectNode hero = getCardNode(player.getHero());

        actionOutput.put("playerIdx", action.getPlayerIdx());
        actionOutput.set("output", hero);
    }

    /**
     * Determines which player's turn it currently is and adds the result to the action output.
     *
     * @param actionOutput the output object for the action
     * @param currPlayer   the current player
     * @param player1      player one
     */
    public static void getPlayerTurn(final ObjectNode actionOutput, final Player currPlayer,
                                     final Player player1) {
        actionOutput.put("output", currPlayer.equals(player1) ? 1 : 2);
    }

    /**
     * Ends the current player's turn, resets the states of their cards, and starts a new round
     * if both players have ended their turns.
     *
     * @param currentPlayer the current player
     * @param opponent      the opponent player
     */
    public static void endPlayerTurn(final Player currentPlayer, final Player opponent) {
        ArrayList<Card> cardsOnTable = new ArrayList<>();
        cardsOnTable.addAll(currentPlayer.getFrontRow());
        cardsOnTable.addAll(currentPlayer.getBackRow());

        for (Card card : cardsOnTable) {
            card.setFrozen(false);
            card.setHasAttacked(false);
        }

        currentPlayer.getHero().setHasAttacked(false);
        currentPlayer.setTurnEnded(true);

        if (currentPlayer.hasTurnEnded() && opponent.hasTurnEnded()) {
            Game.incrementRound();

            currentPlayer.setTurnEnded(false);
            opponent.setTurnEnded(false);

            Game.startNewRound();
        }
    }

    /**
     * Retrieves the cards in the player's hand and adds them to the action output.
     *
     * @param output the output object for the action
     * @param action the action input
     * @param player the player whose cards are being retrieved
     */
    public static void getCardsInHand(final ObjectNode output, final ActionsInput action,
                                      final Player player) {
        output.put("playerIdx", action.getPlayerIdx());
        ArrayNode cardsInHand = createCardsArrayNode(player.getCardsInHand());
        output.set("output", cardsInHand);
    }

    /**
     * Retrieves the player's current mana and adds it to the action output.
     *
     * @param output the output object for the action
     * @param action the action input
     * @param player the player whose mana is being retrieved
     */
    public static void getPlayerMana(final ObjectNode output, final ActionsInput action,
                                     final Player player) {
        output.put("output", player.getManaManager().getMana());
        output.put("playerIdx", action.getPlayerIdx());
    }

    /**
     * Retrieves all cards on the table and adds them to the action output.
     *
     * @param output  the output object for the action
     * @param player1 player one
     * @param player2 player two
     */
    public static void getCardsOnTable(final ObjectNode output, final Player player1,
                                       final Player player2) {
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode table = objectMapper.createArrayNode();

        table.add(createCardsArrayNode(player2.getBackRow()));
        table.add(createCardsArrayNode(player2.getFrontRow()));
        table.add(createCardsArrayNode(player1.getFrontRow()));
        table.add(createCardsArrayNode(player1.getBackRow()));

        output.set("output", table);
    }

    /**
     * Places a card from the player's hand onto the table.
     *
     * @param output        the output object for the action
     * @param action        the action input
     * @param currentPlayer the current player performing the action
     * @param handIndex     the index of the card in the player's hand
     */
    public static void placeCard(final ArrayNode output, final ActionsInput action,
                                 final Player currentPlayer, final int handIndex) {
        Card card = currentPlayer.getCardsInHand().get(handIndex);

        if (card.getMana() > currentPlayer.getManaManager().getMana()) {
            throwException(output, Exceptions.NOT_ENOUGH_MANA, action);
            return;
        }

        if (currentPlayer.rowForMinion(card.getName()).size() >= Constants.MAX_CARDS_IN_ROW) {
            throwException(output, Exceptions.ROW_IS_FULL, action);
            return;
        }

        currentPlayer.addCardIndex(handIndex);
        currentPlayer.getManaManager().subtractMana(card.getMana());
        currentPlayer.removeCardFromHand(handIndex);
    }

    /**
     * Executes an attack between two cards.
     *
     * @param outputData the output object for the action
     * @param action     the action input
     * @param player1    player one
     * @param player2    player two
     */
    public static void cardUsesAttack(final ArrayNode outputData, final ActionsInput action,
                                      final Player player1, final Player player2) {
        int attackX = action.getCardAttacker().getX();
        int attackY = action.getCardAttacker().getY();
        int defendX = action.getCardAttacked().getX();
        int defendY = action.getCardAttacked().getY();

        Player attacker = (attackX <= Constants.ONE) ? player2 : player1;
        Player defender = (defendX >= Constants.TWO) ? player1 : player2;

        ArrayList<Minion> attackingRow = getRowIndex(attacker, attackX);
        ArrayList<Minion> defendingRow = getRowIndex(defender, defendX);

        Minion attackingCard = attackingRow.get(attackY);
        Minion defendingCard = defendingRow.get(defendY);

        if (attacker.equals(defender)) {
            throwException(outputData, Exceptions.NOT_ENEMY_CARD, action);
            return;
        }

        if (attackingCard.getHasAttacked()) {
            throwException(outputData, Exceptions.CARD_ATTACKED, action);
            return;
        }

        if (attackingCard.isFrozen()) {
            throwException(outputData, Exceptions.FROZEN, action);
            return;
        }

        if (!isTank(defendingCard) && tankExists(defender.getFrontRow())) {
            throwException(outputData, Exceptions.TANK_NOT_ATTACKED, action);
            return;
        }

        attackingCard.setHasAttacked(true);
        defendingCard.setHealth(defendingCard.getHealth() - attackingCard.getAttackDamage());

        if (defendingCard.getHealth() <= 0) {
            defendingRow.remove(defendingCard);
        }
    }

    /**
     * Retrieves the card at a specific position on the table.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing the position (x, y)
     * @param player1      player one
     * @param player2      player two
     */
    public static void getCardAtPosition(final ObjectNode actionOutput, final ActionsInput action,
                                         final Player player1, final Player player2) {
        int x = action.getX();

        ArrayList<Minion> row =  new ArrayList<>();
        if (x == Constants.ZERO) {
            row = player2.getBackRow();
        } else if (x == Constants.ONE) {
            row = player2.getFrontRow();
        } else if (x == Constants.TWO) {
            row = player1.getFrontRow();
        } else if (x == Constants.THREE) {
            row = player1.getBackRow();
        }

        int y = action.getY();

        actionOutput.put("x", x);
        actionOutput.put("y", y);

        if (!row.isEmpty() && y >= 0 && y < row.size()) {
            ObjectNode cardNode = getCardNode(row.get(y));
            actionOutput.set("output", cardNode);
        } else {
            actionOutput.put("output", Exceptions.NO_CARD_AT_POSITION);
        }
    }

    /**
     * Executes a special ability of a card on a target card.
     *
     * @param outputData the output object for the action
     * @param action     the action input containing attacker and target details
     * @param player1    player one
     * @param player2    player two
     */
    public static void cardUsesAbility(final ArrayNode outputData, final ActionsInput action,
                                       final Player player1, final Player player2) {
        int attackX = action.getCardAttacker().getX();
        int attackY = action.getCardAttacker().getY();
        int defendX = action.getCardAttacked().getX();
        int defendY = action.getCardAttacked().getY();

        Player attacker = (attackX <= Constants.ONE) ? player2 : player1;
        Player defender = (defendX >= Constants.TWO) ? player1 : player2;

        ArrayList<Minion> attackingRow = getRowIndex(attacker, attackX);
        ArrayList<Minion> defendingRow = getRowIndex(defender, defendX);

        Minion attackingCard;
        try {
            attackingCard = attackingRow.get(attackY);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
        Minion defendingCard = defendingRow.get(defendY);

        if (attackingCard.isFrozen()) {
            throwException(outputData, Exceptions.FROZEN, action);
            return;
        }

        if (attackingCard.getHasAttacked()) {
            throwException(outputData, Exceptions.CARD_ATTACKED, action);
            return;
        }
        if (!attacker.equals(defender) && attackingCard.getName().matches("Disciple")) {
            throwException(outputData, Exceptions.NOT_OWN_CARD, action);
            return;
        }

        if (Arrays.asList("Ripper", "Miraj", "The Cursed One").contains(attackingCard.getName())) {
            if (defender.equals(attacker)) {
                throwException(outputData, Exceptions.NOT_ENEMY_CARD, action);
                return;
            }

            if (!isTank(defendingCard) && tankExists(defender.getFrontRow())) {
                throwException(outputData, Exceptions.TANK_NOT_ATTACKED, action);
                return;
            }
        }

        attackingCard.useAbility(attackingCard, defendingCard, defendingRow);
    }

    /**
     * Executes a special ability of a card on a target card.
     *
     * @param outputData the output object for the action
     * @param action     the action input containing attacker and target details
     * @param player1    player one
     * @param player2    player two
     */
    public static void useAttackHero(final ArrayNode outputData, final ActionsInput action,
                                     final Player player1, final Player player2) {
        int attackX = action.getCardAttacker().getX();
        int attackY = action.getCardAttacker().getY();

        Player attacker = (attackX <= Constants.ONE) ? player2 : player1;
        Player defender = (attacker.equals(player1)) ? player2 : player1;

        ArrayList<Minion> attackingRow = getRowIndex(attacker, attackX);

        Minion attackingCard = attackingRow.get(attackY);
        Hero attackedHero = defender.getHero();

        if (attackingCard.isFrozen()) {
            throwException(outputData, Exceptions.FROZEN, action);
            return;
        }

        if (attackingCard.getHasAttacked()) {
            throwException(outputData, Exceptions.CARD_ATTACKED, action);
            return;
        }

        if (!isTank(attackedHero) && tankExists(defender.getFrontRow())) {
            throwException(outputData, Exceptions.TANK_NOT_ATTACKED, action);
            return;
        }

        attackedHero.setHealth(attackedHero.getHealth() - attackingCard.getAttackDamage());
        attackingCard.setHasAttacked(true);

        if (attackedHero.getHealth() <= 0) {
            ObjectNode node = new ObjectMapper().createObjectNode();

            if (attacker.equals(player1)) {
                Game.incrementPlayerOneWins();
            } else {
                Game.incrementPlayerTwoWins();
            }

            node.put("gameEnded", "Player "
                    + ((attacker.equals(player1)) ? "one" : "two")
                    + " killed the enemy hero.");
            outputData.add(node);
        }

    }

    /**
     * Executes the hero's special ability on a specified row.
     *
     * @param output        the output object for the action
     * @param action        the action input containing the affected row and hero details
     * @param currentPlayer the player currently executing the action
     * @param player1       player one
     * @param player2       player two
     */
    public static void useHeroAbility(final ArrayNode output, final ActionsInput action,
                                      final Player currentPlayer, final Player player1,
                                      final Player player2) {

        Hero hero = currentPlayer.getHero();
        if (hero.getMana() > currentPlayer.getManaManager().getMana()) {
            throwException(output, Exceptions.NOT_ENOUGH_MANA_HERO, action);
            return;
        }
        if (hero.getHasAttacked()) {
            throwException(output, Exceptions.HERO_ATTACKED, action);
            return;
        }

        Player target = currentPlayer;
        ArrayList<Minion> affectedRow = new ArrayList<>();
        if (action.getAffectedRow() == Constants.ZERO) {
            target = player2;
            affectedRow = player2.getBackRow();
        } else if (action.getAffectedRow() == Constants.ONE) {
            target = player2;
            affectedRow = player2.getFrontRow();
        } else if (action.getAffectedRow() == Constants.TWO) {
            target = player1;
            affectedRow = player1.getFrontRow();
        } else if (action.getAffectedRow() == Constants.THREE) {
            target = player1;
            affectedRow = player1.getBackRow();
        }

        if (Arrays.asList("Lord Royce", "Empress Thorina").contains(hero.getName())
                && target.equals(currentPlayer)) {
            throwException(output, Exceptions.ROW_NOT_ENEMY, action);
            return;
        }
        if (Arrays.asList("General Kocioraw", "King Mudface").contains(hero.getName())
                && !target.equals(currentPlayer)) {
            throwException(output, Exceptions.ROW_NOT_OWN, action);
            return;
        }

        hero.useAbility(affectedRow);
        currentPlayer.getManaManager().subtractMana(hero.getMana());
    }

    /**
     * Retrieves all frozen cards on the table and adds them to the output.
     *
     * @param output  the output object for the action
     * @param player1 player one
     * @param player2 player two
     */
    public static void getFrozenCardsOnTable(final ObjectNode output, final Player player1,
                                             final Player player2) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode node = objectMapper.createArrayNode();

        ArrayList<Card> cardsOnTable = new ArrayList<>();
        cardsOnTable.addAll(player2.getBackRow());
        cardsOnTable.addAll(player2.getFrontRow());
        cardsOnTable.addAll(player1.getFrontRow());
        cardsOnTable.addAll(player1.getBackRow());

        for (Card card : cardsOnTable) {
            if (card.isFrozen()) {
                node.add(getCardNode(card));
            }
        }

        output.set("output", node);
    }

    /**
     * Retrieves the total number of games played and adds it to the action output.
     *
     * @param actionOutput the output object for the action
     */
    public static void getTotalGamesPlayed(final ObjectNode actionOutput) {
        actionOutput.put("output", Game.getPlayerOneWins() + Game.getPlayerTwoWins());
    }

    /**
     * Retrieves the number of wins by player one and adds it to the action output.
     *
     * @param actionOutput the output object for the action
     */
    public static void getPlayerOneWins(final ObjectNode actionOutput) {
        actionOutput.put("output", Game.getPlayerOneWins());
    }

    /**
     * Retrieves the number of wins by player two and adds it to the action output.
     *
     * @param actionOutput the output object for the action
     */
    public static void getPlayerTwoWins(final ObjectNode actionOutput) {
        actionOutput.put("output", Game.getPlayerTwoWins());
    }
}
