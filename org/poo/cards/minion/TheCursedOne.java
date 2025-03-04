package org.poo.cards.minion;

import org.poo.cards.Card;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents The Cursed One minion, which has the ability to swap the health
 * and attack damage to a target card.
 */
public class TheCursedOne extends Minion {

    /**
     * Constructs an instance of The Cursed One minion.
     *
     * @param mana         the mana cost required to summon this minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or characteristics
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @param attackDamage the attack damage value of the minion
     */
    public TheCursedOne(final int mana, final int health, final String description,
                        final ArrayList<String> colors, final String name, final int attackDamage) {
        super(mana, health, description, colors, name, attackDamage);
    }

    /**
     * Uses The Cursed One's ability to swap the health and attack damage to a target card.
     * If the target's health becomes 0 or less after the swap, it is removed from the row.
     *
     * @param attacker the card using the ability
     * @param target   the card whose health and attack damage will be swapped
     * @param row      the row of minions from which the target may be removed
     */
    public void useAbility(final Card attacker, final Card target, final ArrayList<Minion> row) {
        int auxHealth = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(auxHealth);

        if (target.getHealth() <= 0) {
            row.remove(target);
        }
        attacker.setHasAttacked(true);
    }
}
