package org.poo.player;

import lombok.Getter;
import lombok.Setter;
import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.game.Constants;

import java.util.ArrayList;

/**
 * Represents a player in the game, managing their hero, cards, mana, and gameplay actions.
 */
@Setter
@Getter
public class Player {

    private ManaManager manaManager;
    private DeckManager deckManager;

    private Hero hero;

    private ArrayList<Minion> cardsInHand = new ArrayList<>();
    private ArrayList<Minion> frontRow = new ArrayList<>();
    private ArrayList<Minion> backRow = new ArrayList<>();

    private int gamesPlayed = 0;
    private boolean turnEnded = false;

    /**
     * Default constructor for initializing a Player with a new mana and deck manager.
     */
    public Player() {
        this.manaManager = new ManaManager();
        this.deckManager = new DeckManager();
    }

    /**
     * Checks if the player's turn has ended.
     *
     * @return true if the turn has ended, false otherwise
     */
    public boolean hasTurnEnded() {
        return turnEnded;
    }

    /**
     * Resets the player's game state, clearing all cards and resetting mana.
     */
    public void resetGameState() {
        cardsInHand.clear();
        frontRow.clear();
        backRow.clear();
        manaManager.resetMana();
    }

    /**
     * Draws a card from the player's deck. If the deck is not empty, the card is added to the hand.
     */
    public void drawCard() {
        Minion card = deckManager.drawCard();
        if (card != null) {
            cardsInHand.add(card);
        }
    }

    /**
     * Resets all card collections for the player, clearing the hand and rows.
     */
    public void resetCards() {
        this.cardsInHand = new ArrayList<>();
        this.frontRow = new ArrayList<>();
        this.backRow = new ArrayList<>();
    }

    /**
     * Removes a card from the player's hand at the specified index.
     *
     * @param index the index of the card to be removed
     */
    public void removeCardFromHand(final int index) {
        cardsInHand.remove(index);
    }

    /**
     * Adds a card from the player's hand to the appropriate row based on its type.
     *
     * @param index the index of the card in the hand to be added to a row
     */
    public void addCardIndex(final int index) {
        if (cardsInHand.isEmpty() || cardsInHand.size() <= index) {
            return;
        }

        Minion card = cardsInHand.get(index);

        rowForMinion(card.getName()).add(card);
    }

    /**
     * Adds the top card from the player's deck to their hand, if the deck is not empty.
     */
    public void addCardInHand() {
        if (!(deckManager.getCurrentDeck().isEmpty())) {
            cardsInHand.add(deckManager.getCurrentDeck().getFirst());
            deckManager.removeCardFromDeck(Constants.ZERO);
        }
    }

    /**
     * Resets the attack state for all cards in both the front and back rows.
     */
    public void resetCardAttacks() {
        for (Card card : backRow) {
            card.setHasAttacked(false);
        }

        for (Card card : frontRow) {
            card.setHasAttacked(false);
        }
    }

    /**
     * Determines the appropriate row for a given minion based on its name.
     *
     * @param cardName the name of the minion
     * @return the row (front or back) that the minion belongs to
     */
    public ArrayList<Minion> rowForMinion(final String cardName) {
        if (cardName.matches("The Ripper")
                || cardName.matches("Miraj")
                || cardName.matches("Goliath")
                || cardName.matches("Warden")) {
            return frontRow;
        } else {
            return backRow;
        }
    }

    /**
     * Retrieves the row of minions based on the row index.
     *
     * @param player    the player whose rows are being queried
     * @param rowIndex  the index of the row (0 or 3 for back row, others for front row)
     * @return the corresponding row of minions
     */
    public static ArrayList<Minion> getRowIndex(final Player player, final int rowIndex) {
        return (rowIndex == Constants.ZERO || rowIndex == Constants.THREE)
                ? player.getBackRow()
                : player.getFrontRow();
    }

}
