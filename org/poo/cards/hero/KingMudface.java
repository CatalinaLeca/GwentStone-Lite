package org.poo.cards.hero;

import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the hero King Mudface with a special ability to increase the health
 * of all target minions.
 */
public class KingMudface extends Hero {

    /**
     * Constructs an instance of King Mudface.
     *
     * @param mana         the mana cost required to use this hero
     * @param description  a description of the hero
     * @param colors       a list of colors associated with the hero
     * @param name         the name of the hero
     * @param attackDamage the attack damage value of the hero
     */
    public KingMudface(final int mana, final String description, final ArrayList<String> colors,
                       final String name, final int attackDamage) {
        super(mana, description, colors, name, attackDamage);
    }

    /**
     * Uses the hero's ability to increase the health of all target minions by 1.
     *
     * @param targets a list of target minions whose health will be boosted
     */
    public void useAbility(final ArrayList<Minion> targets) {
        for (Card target : targets) {
            target.setHealth(target.getHealth() + 1);
        }
        this.setHasAttacked(true);
    }
}
