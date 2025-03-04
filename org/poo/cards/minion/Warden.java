package org.poo.cards.minion;

import org.poo.cards.Card;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the Warden minion, which inherits from the Minion class.
 * The Warden's specific ability is yet to be implemented.
 */
public class Warden extends Minion {

    /**
     * Constructs an instance of the Warden minion.
     *
     * @param mana         the mana cost required to summon this minion
     * @param health       the health value of the minion
     * @param description  a description of the minion's abilities or characteristics
     * @param colors       a list of colors associated with the minion
     * @param name         the name of the minion
     * @param attackDamage the attack damage value of the minion
     */
    public Warden(final int mana, final int health, final String description,
                  final ArrayList<String> colors, final String name, final int attackDamage) {
        super(mana, health, description, colors, name, attackDamage);
    }

    /**
     * Uses the Warden's ability. Currently, this method is unimplemented.
     *
     * @param attacker the card using the ability
     * @param target   the target card of the ability
     * @param row      the row of minions involved in the interaction
     */
    @Override
    public void useAbility(final Card attacker, final Card target, final ArrayList<Minion> row) {
        // Ability logic is not necessary.
    }
}
