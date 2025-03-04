package org.poo.cards.hero;

import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the hero General Kocioraw with a special ability to boost the attack damage
 * to all target minions.
 */
public class GeneralKocioraw extends Hero {

    /**
     * Constructs an instance of General Kocioraw.
     *
     * @param mana         the mana cost required to use this hero
     * @param description  a description of the hero
     * @param colors       a list of colors associated with the hero
     * @param name         the name of the hero
     * @param attackDamage the attack damage value of the hero
     */
    public GeneralKocioraw(final int mana, final String description, final ArrayList<String> colors,
                           final String name, final int attackDamage) {
        super(mana, description, colors, name, attackDamage);
    }

    /**
     * Uses the hero's ability to increase the attack damage of all target minions by 1.
     *
     * @param targets a list of target minions whose attack damage will be boosted
     */
    public void useAbility(final ArrayList<Minion> targets) {
        for (Card target : targets) {
            target.setAttackDamage(target.getAttackDamage() + 1);
        }

        this.setHasAttacked(true);
    }
}
