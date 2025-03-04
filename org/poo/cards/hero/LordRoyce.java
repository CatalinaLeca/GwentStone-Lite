package org.poo.cards.hero;

import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the hero Lord Royce with a special ability to freeze all target minions.
 */
public class LordRoyce extends Hero {

    /**
     * Constructs an instance of Lord Royce.
     *
     * @param mana         the mana cost required to use this hero
     * @param description  a description of the hero
     * @param colors       a list of colors associated with the hero
     * @param name         the name of the hero
     * @param attackDamage the attack damage value of the hero
     */
    public LordRoyce(final int mana, final String description, final ArrayList<String> colors,
                     final String name, final int attackDamage) {
        super(mana, description, colors, name, attackDamage);
    }

    /**
     * Uses the hero's ability to freeze all target minions, preventing them from
     * attacking or performing other actions.
     *
     * @param targets a list of target minions that will be frozen
     */
    public void useAbility(final ArrayList<Minion> targets) {
        for (Card target : targets) {
            target.setFrozen(true);
        }
        this.setHasAttacked(true);
    }
}
