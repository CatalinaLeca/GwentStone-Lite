package org.poo.cards.hero;

import org.poo.cards.Card;
import org.poo.cards.Hero;
import org.poo.cards.Minion;

import java.util.ArrayList;

/**
 * Represents the hero Empress Thorina with a special ability to remove the card with the highest
 * health.
 */
public class EmpressThorina extends Hero {

    /**
     * Constructs an instance of Empress Thorina.
     *
     * @param mana         the mana cost required to use this hero
     * @param description  a description of the hero
     * @param colors       a list of colors associated with the hero
     * @param name         the name of the hero
     * @param attackDamage the attack damage value of the hero
     */
    public EmpressThorina(final int mana, final String description, final ArrayList<String> colors,
                          final String name, final int attackDamage) {
        super(mana, description, colors, name, attackDamage);
    }

    /**
     * Uses the hero's ability to find and remove the card with the highest health
     * from the list of target minions.
     *
     * @param targets a list of target minions affected by the ability
     */
    public void useAbility(final ArrayList<Minion> targets) {
        Card maxHealthCard = new Card();
        maxHealthCard.setHealth(-1);

        for (Card card : targets) {
            if (card.getHealth() > maxHealthCard.getHealth()) {
                maxHealthCard = card;
            }
        }

        targets.remove(maxHealthCard);
        this.setHasAttacked(true);
    }
}
