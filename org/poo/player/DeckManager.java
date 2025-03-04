package org.poo.player;

import lombok.Getter;
import lombok.Setter;
import org.poo.cards.Minion;
import org.poo.fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.poo.cards.Card.convertCardInputToCard;

/**
 * Manages the player's decks, including shuffling, drawing cards, and handling multiple decks.
 */
public class DeckManager {

    @Setter
    @Getter
    private int nrDecks;
    @Setter
    @Getter
    private int nrCardsInDeck;
    @Setter
    @Getter
    private int currentDeckIndex;
    private ArrayList<Minion> currentDeck = new ArrayList<>();
    private ArrayList<ArrayList<Minion>> allDecks = new ArrayList<>();

    /**
     * Gets the current active deck.
     *
     * @return the current deck as an ArrayList of Minion objects
     */
    public ArrayList<Minion> getCurrentDeck() {
        return currentDeck;
    }

    /**
     * Sets the current deck to the specified deck.
     *
     * @param deck the new deck to set as the current deck
     */
    public void setCurrentDeck(final ArrayList<Minion> deck) {
        this.currentDeck = new ArrayList<>(deck);
    }

    /**
     * Adds a new deck to the collection of all decks.
     *
     * @param deck the new deck to add
     */
    public void addDeck(final ArrayList<Minion> deck) {
        allDecks.add(new ArrayList<>(deck));
    }

    /**
     * Shuffles the current deck using a specified seed.
     *
     * @param seed a string representing the seed for shuffling
     */
    public void shuffleDeck(final String seed) {
        Random random = new Random(Long.parseLong(seed));
        Collections.shuffle(currentDeck, random);
    }

    /**
     * Draws a card from the current deck. If the deck is empty, returns null.
     *
     * @return the drawn card as a Minion object or null if the deck is empty
     */
    public Minion drawCard() {
        if (!currentDeck.isEmpty()) {
            return currentDeck.remove(0); // Replace `removeFirst` with `remove(0)`
        }
        return null;
    }

    /**
     * Replaces the current deck with a copy of the specified deck.
     *
     * @param deck the new deck to set as the current deck
     */
    public void replaceCurrentDeck(final ArrayList<Minion> deck) {
        this.currentDeck = copyDeck(deck);
    }

    /**
     * Gets all decks as a list of deck collections.
     *
     * @return the list of all decks
     */
    public ArrayList<ArrayList<Minion>> getAllDecks() {
        return allDecks;
    }

    /**
     * Sets all decks to the specified list of decks and updates the number of decks
     * and the number of cards in each deck.
     *
     * @param decks the new collection of all decks
     */
    public void setAllDecks(final ArrayList<ArrayList<Minion>> decks) {
        this.allDecks = decks;
        this.nrDecks = decks.size();
        this.nrCardsInDeck = decks.get(0).size(); // Replace `getFirst` with `get(0)`
    }

    /**
     * Converts a collection of CardInput objects into a collection of decks containing Minions.
     *
     * @param decksInput the list of CardInput objects to convert
     * @return a collection of decks as a list of Minion objects
     */
    public ArrayList<ArrayList<Minion>> getConvertedDecks(final ArrayList<ArrayList<CardInput>>
                                                                  decksInput) {
        ArrayList<ArrayList<Minion>> cardDecks = new ArrayList<>();
        for (ArrayList<CardInput> deckInput : decksInput) {
            ArrayList<Minion> deck = new ArrayList<>();
            for (CardInput cardInput : deckInput) {
                deck.add((Minion) convertCardInputToCard(cardInput));
            }
            cardDecks.add(deck);
        }
        return cardDecks;
    }

    /**
     * Converts and sets a list of decks using CardInput objects.
     *
     * @param decks the list of decks as CardInput objects
     */
    public void setConvertedDecks(final ArrayList<ArrayList<CardInput>> decks) {
        this.allDecks = getConvertedDecks(decks);
        this.nrDecks = decks.size();
        this.nrCardsInDeck = decks.get(0).size(); // Replace `getFirst` with `get(0)`
    }

    /**
     * Removes a card from the current deck at the specified index.
     *
     * @param index the index of the card to remove
     */
    public void removeCardFromDeck(final int index) {
        if (!currentDeck.isEmpty() && currentDeck.size() > index) {
            currentDeck.remove(index);
        }
    }

    /**
     * Creates a deep copy of the specified deck.
     *
     * @param deck the deck to copy
     * @return a new deck with copied Minion objects
     */
    public static ArrayList<Minion> copyDeck(final ArrayList<Minion> deck) {
        ArrayList<Minion> newDeck = new ArrayList<>();
        for (Minion card : deck) {
            Minion minion = Minion.create(card.getMana(), card.getAttackDamage(), card.getHealth(),
                    card.getDescription(), card.getColors(), card.getName());
            newDeck.add(minion);
        }
        return newDeck;
    }
}
