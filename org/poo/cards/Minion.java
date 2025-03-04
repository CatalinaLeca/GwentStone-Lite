package org.poo.cards;

import org.poo.cards.minion.Berserker;
import org.poo.cards.minion.Disciple;
import org.poo.cards.minion.Goliath;
import org.poo.cards.minion.Miraj;
import org.poo.cards.minion.Sentinel;
import org.poo.cards.minion.TheCursedOne;
import org.poo.cards.minion.TheRipper;
import org.poo.cards.minion.Warden;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Represents a generic Minion card, which extends the functionality of the Card class.
 * Minions have specific abilities that can be implemented in subclasses.
 */
public abstract class Minion extends Card {

    /**
     * Constructs a Minion object with the specified properties.
     *
     * @param mana         the mana cost of the minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or effects
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @param attackDamage the attack damage value of the minion
     */
    public Minion(final int mana, final int health, final String description,
                  final ArrayList<String> colors, final String name, final int attackDamage) {
        super(mana, health, description, colors, name, attackDamage);
    }

    /**
     * Resets the state of the minion, setting `hasAttacked` and `frozen` to false.
     * This method overrides the resetState method from the Card class.
     */
    @Override
    public void resetState() {
        super.resetState();
    }

    /**
     * Abstract method for the minion's specific ability. Must be implemented in subclasses.
     *
     * @param attacker the minion that is using the ability
     * @param target   the target card affected by the ability
     * @param row      the row of minions involved in the interaction
     */
    public abstract void useAbility(Card attacker, Card target, ArrayList<Minion> row);

    /**
     * Creates a specific Minion instance based on the provided name.
     *
     * @param mana         the mana cost of the minion
     * @param attackDamage the attack damage value of the minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or effects
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @return a specific Minion instance based on the provided name
     * @throws IllegalStateException if the name does not match any known minion type
     */
    public static Minion create(final int mana, final int attackDamage, final int health,
                                final String description, final ArrayList<String> colors,
                                final String name) {
        return switch (name) {
            case "Berserker" -> new Berserker(mana, health, description, colors, name,
                    attackDamage);
            case "Disciple" -> new Disciple(mana, health, description, colors, name, attackDamage);
            case "Goliath" -> new Goliath(mana, health, description, colors, name, attackDamage);
            case "Miraj" -> new Miraj(mana, health, description, colors, name, attackDamage);
            case "Sentinel" -> new Sentinel(mana, health, description, colors, name, attackDamage);
            case "The Cursed One" -> new TheCursedOne(mana, health, description, colors, name,
                    attackDamage);
            case "The Ripper" -> new TheRipper(mana, health, description, colors, name,
                    attackDamage);
            case "Warden" -> new Warden(mana, health, description, colors, name, attackDamage);
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    /**
     * Creates a specific Minion instance based on a CardInput object.
     *
     * @param cardInput the CardInput object containing the minion's data
     * @return a specific Minion instance based on the provided data
     */
    public static Minion create(final CardInput cardInput) {
        return create(cardInput.getMana(), cardInput.getAttackDamage(), cardInput.getHealth(),
                cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
    }
}
