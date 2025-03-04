package org.poo.cards.minion;

import org.poo.cards.Card;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the Disciple minion, which inherits from the Minion class and has
 * the ability to heal another card.
 */
public class Disciple extends Minion {

    /**
     * Constructs an instance of the Disciple minion.
     *
     * @param mana         the mana cost required to summon this minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or characteristics
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @param attackDamage the attack damage value of the minion
     */
    public Disciple(final int mana, final int health, final String description,
                    final ArrayList<String> colors, final String name, final int attackDamage) {
        super(mana, health, description, colors, name, attackDamage);
    }

    /**
     * Uses the Disciple's ability to heal the target card by increasing its health by 2.
     *
     * @param attacker the Disciple minion that is using the ability
     * @param target   the card that will be healed
     * @param row      the row of minions involved in the interaction (not used in this ability)
     */
    @Override
    public void useAbility(final Card attacker, final Card target, final ArrayList<Minion> row) {
        target.setHealth(target.getHealth() + 2);
        attacker.setHasAttacked(true);
    }
}
