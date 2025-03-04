package org.poo.player;

import lombok.Getter;
import lombok.Setter;
import org.poo.game.Constants;

/**
 * Manages the player's mana during the game, including incrementing,
 * decrementing, and resetting mana values.
 */
public class ManaManager {

    @Getter
    @Setter
    private int mana = 1;
    private int manaIncrement = 1;

    /**
     * Increments the player's mana. The amount of increment increases
     * each turn up to a maximum defined by `Constants.TEN`.
     */
    public void incrementMana() {
        if (manaIncrement < Constants.TEN) {
            manaIncrement++;
        }
        this.mana += manaIncrement;
    }

    /**
     * Subtracts a specified amount of mana from the player's total mana.
     *
     * @param amount the amount of mana to subtract
     */
    public void subtractMana(final int amount) {
        this.mana -= amount;
    }

    /**
     * Resets the player's mana and the mana increment to their initial values.
     */
    public void resetMana() {
        this.mana = 1;
        this.manaIncrement = 1;
    }
}
