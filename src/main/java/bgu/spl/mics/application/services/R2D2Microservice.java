package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.ShieldedEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.DeactivationEvent;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    final long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class, (DeactivationEvent e) -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException interruptedException) {
            }
            diary.setR2D2Deactivate(System.currentTimeMillis());
            complete(e, true);
            ShieldedEvent shieldedEvent = new ShieldedEvent();
            Future<Boolean> shildedFuture = sendEvent(shieldedEvent);
        });
        subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast b) -> {
            terminate();
            diary.setR2D2Terminate(System.currentTimeMillis());
        });
        counter.CountDown();
    }
}