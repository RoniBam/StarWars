package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    final Attack[] attacks;
    final Diary diary;
    private AtomicInteger countAttacks;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        diary = Diary.getDiary();
        countAttacks = new AtomicInteger(attacks.length);
    }


    @Override
    protected void initialize() {
        while (counter.getValue().intValue() != 0) {
        }
        subscribeEvent(ShieldedEvent.class, (ShieldedEvent e) -> {
            complete(e, true);
            BombDestroyerEvent bombDestroyerEvent = new BombDestroyerEvent();
            Future<Boolean> bombFuture = sendEvent(bombDestroyerEvent);
        });

        subscribeEvent(BombExplodedEvent.class, (BombExplodedEvent e) -> {
            complete(e, true);
            TerminationBroadcast terminationBroadcast = new TerminationBroadcast();
            sendBroadcast(terminationBroadcast);
        });

        subscribeEvent(attackFinishedEvent.class, (attackFinishedEvent e) -> {
            synchronized (this) {
                int update = countAttacks.decrementAndGet();
                if (update == 0) {
                    DeactivationEvent deactivationEvent = new DeactivationEvent();
                    Future<Boolean> deactivationFuture = sendEvent(deactivationEvent);
                    complete(e,true);
                }
            }
        });

        subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast b) -> {
            terminate();
            diary.setLeiaTerminate(System.currentTimeMillis());
        });
        List<Future> attackFutures = new ArrayList<>();
        for (Attack element : attacks) {
            AttackEvent newAttack = new AttackEvent(element);
            Future<Boolean> future = sendEvent(newAttack);
            synchronized (this) {
                attackFutures.add(future);
            }
        }
    }
}
