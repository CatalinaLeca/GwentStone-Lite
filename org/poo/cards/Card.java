package org.poo.cards;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CardInput;
import org.poo.game.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic card with properties such as mana, health, description, colors,
 * name, attack damage, and states such as whether it has attacked or is frozen.
 */
@Getter
@Setter
public class Card {

    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int attackDamage;
    private boolean hasAttacked;
    private boolean frozen;

    /**
     * Default constructor for creating a Card object without initialization.
     */
    public Card() {
    }

    /**
     * Constructs a Card object with specified properties.
     *
     * @param mana         the mana cost of the card
     * @param health       the health value of the card
     * @param description  a description of the card's abilities or effects
     * @param colors       a list of colors associated with the card
     * @param name         the name of the card
     * @param attackDamage the attack damage value of the card
     */
    public Card(final int mana, final int health, final String description,
                final ArrayList<String> colors, final String name, final int attackDamage) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.attackDamage = attackDamage;
        this.hasAttacked = false;
        this.frozen = false;
    }

    /**
     * Resets the state of the card, setting `hasAttacked` and `frozen` to false.
     */
    public void resetState() {
        this.hasAttacked = false;
        this.frozen = false;
    }

    /**
     * Checks if the card is eligible to attack.
     *
     * @return true if the card has not attacked and is not frozen, false otherwise
     */
    public boolean canAttack() {
        return !this.hasAttacked && !this.frozen;
    }

    private static final List<String> MINIONS = List.of(
            "Berserker",
            "Disciple",
            "Goliath",
            "Miraj",
            "Sentinel",
            "The Cursed One",
            "The Ripper",
            "Warden"
    );

    private static final List<String> HEROES = List.of(
            "Empress Thorina",
            "General Kocioraw",
            "King Mudface",
            "Lord Royce"
    );

    /**
     * Determines the type of card based on its name.
     *
     * @param name the name of the card
     * @return Constants.ONE if it is a minion, Constants.TWO if it is a hero,
     * or Constants.ZERO if it does not match either
     */
    public static int getCardType(final String name) {
        if (MINIONS.contains(name)) {
            return Constants.ONE;
        } else if (HEROES.contains(name)) {
            return Constants.TWO;
        }
        return Constants.ZERO;
    }

    /**
     * Checks if a given card is a tank (e.g., Goliath or Warden).
     *
     * @param card the card to check
     * @return true if the card is a tank, false otherwise
     */
    public static boolean isTank(final Card card) {
        return card.name.equals("Goliath") || card.name.equals("Warden");
    }

    /**
     * Determines if a tank exists in a given row of minions.
     *
     * @param row the row of minions to check
     * @return true if a tank is present, false otherwise
     */
    public static boolean tankExists(final ArrayList<Minion> row) {
        for (Card card : row) {
            if (isTank(card)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a CardInput object to a Card object, creating either a Minion or Hero.
     *
     * @param cardInput the CardInput object to convert
     * @return the corresponding Card object
     */
    public static Card convertCardInputToCard(final CardInput cardInput) {
        if (getCardType(cardInput.getName()) == Constants.ONE) {
            return Minion.create(cardInput);
        } else {
            return Hero.create(cardInput);
        }
    }

    /**
     * Returns whether the card has attacked.
     *
     * @return true if the card has attacked, false otherwise
     */
    public boolean getHasAttacked() {
        return this.hasAttacked;
    }

}
