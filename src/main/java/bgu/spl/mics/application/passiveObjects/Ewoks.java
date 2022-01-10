package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.AttackEvent;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private Vector <Ewok> ewoksList;
    private static class SingletonHolder {
        private static Ewoks instance= new Ewoks();
    }

    private Ewoks (){
        ewoksList=new Vector<>();
    }


    public synchronized void getResourcesForAttack(AttackEvent c) {
        for (int i = 1; i < ewoksList.size(); i++) {   // resource ordering acquire
            boolean contains = false;
            for (int j = 1; j < c.getAttackList().length && !contains; j++) {
                if (c.getAttackList()[j]==i) {
                    contains = true;
                    Ewok current = ewoksList.get(c.getAttackList()[j]);
                    if (current.getAvailability()) {
                        current.acquire();
                    } else {
                        try {
                            wait();
                        } catch (InterruptedException e) { }
                    }
                }
            }
        }
    }

    public synchronized void releaseResourcesAfterAttack (AttackEvent c){
        for (int i=1;i<ewoksList.size();i++) {   // resource ordering release
            boolean contains = false;
            for (int j = 1; j < c.getAttackList().length && !contains; j++) {
                if (c.getAttackList()[j] == i) {
                    contains = true;
                    Ewok current = ewoksList.get(c.getAttackList()[j]);
                    current.release();
                    notifyAll();
                }
            }
        }
    }

    public static Ewoks getEwoks(){
        return Ewoks.SingletonHolder.instance;
    }

    public Vector<Ewok> getEwoksList() {
        return ewoksList;
    }

    public void addEwok (Ewok ewok){
        ewoksList.add(ewok);
    }
}
