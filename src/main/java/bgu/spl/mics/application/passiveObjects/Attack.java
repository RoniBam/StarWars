package bgu.spl.mics.application.passiveObjects;


import java.util.ArrayList;

/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final ArrayList<Integer> serials;
    final int duration;

    /**
     * Constructor.
     */

    public Attack(ArrayList<Integer> serialNumbers, int duration) {
        this.serials = serialNumbers;
        this.duration = duration;
    }

    public ArrayList<Integer> getAttackList (){
        return this.serials;
    }

    public int getAttackDuration (){
        return this.duration;
    }
}
