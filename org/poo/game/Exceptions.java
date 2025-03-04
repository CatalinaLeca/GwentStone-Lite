package org.poo.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;

/**
 * Handles game-related exceptions by providing descriptive error messages
 * and generating structured JSON output for each exception scenario.
 */
public final class Exceptions {

    private Exceptions() {
        // Private constructor to prevent instantiation
    }

    // Constants for various error messages
    public static final String NOT_ENOUGH_MANA = "Not enough mana to place card on table.";
    public static final String ROW_IS_FULL = "Cannot place card on table since row is full.";
    public static final String NOT_ENEMY_CARD = "Attacked card does not belong to the enemy.";
    public static final String ROW_NOT_FROM_ENEMY = "Chosen row does not belong to the enemy.";
    public static final String ROW_FULL = "Cannot steal enemy card since the player's row is full.";
    public static final String NO_CARD_AT_POSITION = "No card available at that position.";
    public static final String CARD_ATTACKED = "Attacker card has already attacked this turn.";
    public static final String FROZEN = "Attacker card is frozen.";
    public static final String TANK_NOT_ATTACKED = "Attacked card is not of type 'Tank'.";
    public static final String NOT_OWN_CARD = "Attacked card does not belong to the current player.";
    public static final String NOT_ENOUGH_MANA_HERO = "Not enough mana to use hero's ability.";
    public static final String HERO_ATTACKED = "Hero has already attacked this turn.";
    public static final String ROW_NOT_ENEMY = "Selected row does not belong to the enemy.";
    public static final String ROW_NOT_OWN = "Selected row does not belong to the current player.";

    /**
     * Generates an exception response based on the given exception name and action input.
     *
     * @param outputData    the output data container
     * @param exceptionName the name of the exception
     * @param action        the action input associated with the exception
     */
    public static void throwException(final ArrayNode outputData, final String exceptionName,
                                      final ActionsInput action) {
        ObjectNode actionOutput = JsonNodeFactory.instance.objectNode();
        outputData.add(actionOutput);
        actionOutput.put("command", action.getCommand());

        switch (exceptionName) {
            case NOT_ENOUGH_MANA -> notEnoughManaToPlaceCard(actionOutput, action);
            case ROW_IS_FULL -> rowIsFull(actionOutput, action);
            case ROW_NOT_FROM_ENEMY -> rowNotFromEnemy(actionOutput, action);
            case ROW_FULL -> noStealRowFull(actionOutput, action);
            case NOT_ENEMY_CARD -> notEnemyCard(actionOutput, action);
            case CARD_ATTACKED -> cardAlreadyAttacked(actionOutput, action);
            case FROZEN -> frozenAttacker(actionOutput, action);
            case TANK_NOT_ATTACKED -> tankNotAttacked(actionOutput, action);
            case NOT_OWN_CARD -> notOwnCard(actionOutput, action);
            case NOT_ENOUGH_MANA_HERO -> notEnoughManaForHero(actionOutput, action);
            case HERO_ATTACKED -> heroAlreadyAttacked(actionOutput, action);
            case ROW_NOT_ENEMY -> rowDoesNotBelongToEnemy(actionOutput, action);
            case ROW_NOT_OWN -> rowIsNotOwn(actionOutput, action);
            default -> {
                // Handle unknown exceptions, if necessary
            }
        }
    }
    /**
     * Adds an error message indicating insufficient mana to place a card.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing hand index details
     */
    public static void notEnoughManaToPlaceCard(final ObjectNode actionOutput,
                                                final ActionsInput action) {
        actionOutput.put("error", NOT_ENOUGH_MANA);
        actionOutput.put("handIdx", action.getHandIdx());
    }

    /**
     * Adds an error message indicating the card being attacked is not an enemy card.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing card details
     */
    public static void notEnemyCard(final ObjectNode actionOutput, final ActionsInput action) {
        printCards(actionOutput, action);
        actionOutput.put("error", NOT_ENEMY_CARD);
    }

    /**
     * Adds an error message indicating the card has already attacked this turn.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing card details
     */
    public static void cardAlreadyAttacked(final ObjectNode actionOutput,
                                           final ActionsInput action) {
        printCards(actionOutput, action);
        actionOutput.put("error", CARD_ATTACKED);
    }

    /**
     * Adds an error message indicating the attacked card is not a tank, while a tank exists.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing card details
     */
    public static void tankNotAttacked(final ObjectNode actionOutput, final ActionsInput action) {
        printCards(actionOutput, action);
        actionOutput.put("error", TANK_NOT_ATTACKED);
    }

    /**
     * Adds an error message indicating the card being attacked is not owned by the current player.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing card details
     */
    public static void notOwnCard(final ObjectNode actionOutput, final ActionsInput action) {
        printCards(actionOutput, action);
        actionOutput.put("error", NOT_OWN_CARD);
    }

    /**
     * Adds an error message indicating the row is full and the card cannot be placed.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing hand index details
     */
    public static void rowIsFull(final ObjectNode actionOutput, final ActionsInput action) {
        actionOutput.put("error", ROW_IS_FULL);
        actionOutput.put("handIdx", action.getHandIdx());
    }

    /**
     * Adds an error message indicating the row does not belong to the enemy.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing row and hand index details
     */
    public static void rowNotFromEnemy(final ObjectNode actionOutput, final ActionsInput action) {
        actionOutput.put("handIdx", action.getHandIdx());
        actionOutput.put("affectedRow", action.getAffectedRow());
        actionOutput.put("error", ROW_NOT_FROM_ENEMY);
    }

    /**
     * Adds an error message indicating the player cannot steal a card because the row is full.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing hand index and affected row details
     */
    public static void noStealRowFull(final ObjectNode actionOutput, final ActionsInput action) {
        actionOutput.put("handIdx", action.getHandIdx());
        actionOutput.put("affectedRow", action.getAffectedRow());
        actionOutput.put("error", ROW_FULL);
    }

    /**
     * Adds an error message indicating the attacker card is frozen and cannot attack.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing card details
     */
    public static void frozenAttacker(final ObjectNode actionOutput, final ActionsInput action) {
        printCards(actionOutput, action);
        actionOutput.put("error", FROZEN);
    }

    /**
     * Adds an error message indicating insufficient mana to use the hero's ability.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing affected row details
     */
    public static void notEnoughManaForHero(final ObjectNode actionOutput,
                                            final ActionsInput action) {
        actionOutput.put("affectedRow", action.getAffectedRow());
        actionOutput.put("error", NOT_ENOUGH_MANA_HERO);
    }

    /**
     * Adds an error message indicating the hero has already attacked this turn.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing affected row details
     */
    public static void heroAlreadyAttacked(final ObjectNode actionOutput,
                                           final ActionsInput action) {
        actionOutput.put("affectedRow", action.getAffectedRow());
        actionOutput.put("error", HERO_ATTACKED);
    }

    /**
     * Adds an error message indicating the selected row does not belong to the enemy.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing affected row details
     */
    public static void rowDoesNotBelongToEnemy(final ObjectNode actionOutput,
                                               final ActionsInput action) {
        actionOutput.put("affectedRow", action.getAffectedRow());
        actionOutput.put("error", ROW_NOT_ENEMY);
    }

    /**
     * Adds an error message indicating the selected row does not belong to the current player.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing affected row details
     */
    public static void rowIsNotOwn(final ObjectNode actionOutput, final ActionsInput action) {
        actionOutput.put("affectedRow", action.getAffectedRow());
        actionOutput.put("error", ROW_NOT_OWN);
    }

    /**
     * Adds details of both the attacker and attacked cards to the output.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing card details
     */
    public static void printCards(final ObjectNode actionOutput, final ActionsInput action) {
        printCardAttacker(actionOutput, action);

        if (Commands.getCommandIndex(action.getCommand()) != Constants.FIVE) {
            printCardAttacked(actionOutput, action);
        }
    }

    /**
     * Adds details of the attacker card to the output.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing attacker card details
     */
    public static void printCardAttacker(final ObjectNode actionOutput, final ActionsInput action) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode cardAttacker = objectMapper.createObjectNode();
        cardAttacker.put("x", action.getCardAttacker().getX());
        cardAttacker.put("y", action.getCardAttacker().getY());
        actionOutput.set("cardAttacker", cardAttacker);
    }

    /**
     * Adds details of the attacked card to the output.
     *
     * @param actionOutput the output object for the action
     * @param action       the action input containing attacked card details
     */
    public static void printCardAttacked(final ObjectNode actionOutput, final ActionsInput action) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode cardAttacked = objectMapper.createObjectNode();
        cardAttacked.put("x", action.getCardAttacked().getX());
        cardAttacked.put("y", action.getCardAttacked().getY());
        actionOutput.set("cardAttacked", cardAttacked);
    }
}
