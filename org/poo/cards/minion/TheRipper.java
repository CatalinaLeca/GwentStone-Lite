package org.poo.cards.minion;

import org.poo.cards.Card;
import org.poo.cards.Minion;
import org.poo.game.Constants;

import java.util.ArrayList;

/**
 * Represents The Ripper minion, which has the ability to decrease the attack damage
 * to a target card by a specified amount.
 */
public class TheRipper extends Minion {

    /**
     * Constructs an instance of The Ripper minion.
     *
     * @param mana         the mana cost required to summon this minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or characteristics
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @param attackDamage the attack damage value of the minion
     */
    public TheRipper(final int mana, final int health, final String description,
                     final ArrayList<String> colors, final String name, final int attackDamage) {
        super(mana, health, description, colors, name, attackDamage);
    }

    /**
     * Uses The Ripper's ability to reduce the attack damage to a target card.
     * If the target's attack damage is less than or equal to 2, it is set to 0.
     *
     * @param attacker the card using the ability
     * @param target   the card whose attack damage will be reduced
     * @param row      the row of minions involved in the interaction (not used in this ability)
     */
    public void useAbility(final Card attacker, final Card target, final ArrayList<Minion> row) {
        if (target.getAttackDamage() > Constants.TWO) {
            target.setAttackDamage(target.getAttackDamage() - Constants.TWO);
        } else {
            target.setAttackDamage(Constants.ZERO);
        }
        attacker.setHasAttacked(true);
    }
}
