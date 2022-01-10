package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.attackFinishedEvent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;


import java.util.concurrent.atomic.AtomicInteger;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    final Ewoks ewoks = Ewoks.getEwoks();
    final Diary diary = Diary.getDiary();

    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class,(AttackEvent)-> {
            ewoks.getResourcesForAttack(AttackEvent);
            try {
                Thread.sleep(AttackEvent.getAttackDuration());
            }
            catch (InterruptedException e) { }
            ewoks.releaseResourcesAfterAttack(AttackEvent);
            complete(AttackEvent,true);
            diary.setC3POFinish(System.currentTimeMillis());
            AtomicInteger update = new AtomicInteger(diary.getTotalAttacks().incrementAndGet());
            diary.setTotalAttacks(update);
            attackFinishedEvent attackFinished=new attackFinishedEvent();
            Future<Boolean>  attackFinishedFuture= sendEvent(attackFinished);
            }
        );

        subscribeBroadcast(TerminationBroadcast.class,(TerminationBroadcast b)-> {
            terminate();
            diary.setC3POTerminate(System.currentTimeMillis());
        });
        counter.CountDown();
    }
}







