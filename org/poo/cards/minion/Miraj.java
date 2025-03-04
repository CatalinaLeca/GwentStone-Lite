package org.poo.cards.minion;

import org.poo.cards.Card;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the Miraj minion, which has the special ability to swap its health with that of a
 * target card.
 */
public class Miraj extends Minion {

    /**
     * Constructs an instance of the Miraj minion.
     *
     * @param mana         the mana cost required to summon this minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or characteristics
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @param attackDamage the attack damage value of the minion
     */
    public Miraj(final int mana, final int health, final String description,
                 final ArrayList<String> colors, final String name, final int attackDamage) {
        super(mana, health, description, colors, name, attackDamage);
    }

    /**
     * Uses Miraj's ability to swap its health with the health of a target card.
     *
     * @param attacker the Miraj minion that is using the ability
     * @param target   the card with which the health will be swapped
     * @param row      the row of minions involved in the interaction (not used in this ability)
     */
    public void useAbility(final Card attacker, final Card target, final ArrayList<Minion> row) {
        int auxHealth = attacker.getHealth();
        attacker.setHealth(target.getHealth());
        target.setHealth(auxHealth);
        attacker.setHasAttacked(true);
    }

}
