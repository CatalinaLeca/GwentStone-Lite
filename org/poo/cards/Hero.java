package org.poo.cards;

import org.poo.cards.hero.EmpressThorina;
import org.poo.cards.hero.GeneralKocioraw;
import org.poo.cards.hero.KingMudface;
import org.poo.cards.hero.LordRoyce;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

import static org.poo.game.Constants.START_HEALTH;

/**
 * Represents a Hero card, which extends the functionality of the Card class.
 * Heroes have unique abilities and a fixed starting health value.
 */
public abstract class Hero extends Card {

    /**
     * Constructs a Hero object with the specified properties.
     *
     * @param mana         the mana cost of the hero
     * @param description  a description of the hero's abilities or effects
     * @param colors       a list of colors associated with the hero
     * @param name         the name of the hero
     * @param attackDamage the attack damage value of the hero
     */
    public Hero(final int mana, final String description, final ArrayList<String> colors,
                final String name, final int attackDamage) {
        super(mana, START_HEALTH, description, colors, name, attackDamage);
    }

    /**
     * Reduces the hero's health by a specified damage amount.
     *
     * @param damage the amount of damage to apply to the hero
     */
    public void takeDamage(final int damage) {
        this.setHealth(this.getHealth() - damage);
    }

    /**
     * Checks if the hero is still alive (health greater than 0).
     *
     * @return true if the hero is alive, false otherwise
     */
    public boolean isAlive() {
        return this.getHealth() > 0;
    }

    /**
     * Resets the state of the hero, setting `hasAttacked` and `frozen` to false.
     * This method overrides the resetState method from the Card class.
     */
    @Override
    public void resetState() {
        super.resetState();
    }

    /**
     * Abstract method for the hero's specific ability. Must be implemented in subclasses.
     *
     * @param targets the list of minions affected by the hero's ability
     */
    public abstract void useAbility(ArrayList<Minion> targets);

    /**
     * Creates a specific Hero instance based on a CardInput object.
     *
     * @param cardInput the CardInput object containing the hero's data
     * @return a specific Hero instance based on the provided data
     * @throws IllegalStateException if the hero's name does not match any known hero type
     */
    public static Hero create(final CardInput cardInput) {
        return switch (cardInput.getName()) {
            case "Empress Thorina" -> new EmpressThorina(cardInput.getMana(),
                    cardInput.getDescription(), cardInput.getColors(), cardInput.getName(),
                    cardInput.getAttackDamage());
            case "General Kocioraw" -> new GeneralKocioraw(cardInput.getMana(),
                    cardInput.getDescription(), cardInput.getColors(), cardInput.getName(),
                    cardInput.getAttackDamage());
            case "King Mudface" -> new KingMudface(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(), cardInput.getName(), cardInput.getAttackDamage());
            case "Lord Royce" -> new LordRoyce(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(), cardInput.getName(), cardInput.getAttackDamage());
            default -> throw new IllegalStateException("Unexpected value: " + cardInput.getName());
        };
    }

}
