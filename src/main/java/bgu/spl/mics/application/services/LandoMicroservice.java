package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombExplodedEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.BombDestroyerEvent;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class LandoMicroservice  extends MicroService {

    final long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent e)-> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException interruptedException) { }
            complete(e,true);
            BombExplodedEvent bombExplodedEvent = new BombExplodedEvent();
            Future<Boolean> bombExplodedFuture = sendEvent(bombExplodedEvent);
        });
        subscribeBroadcast(TerminationBroadcast.class,(TerminationBroadcast b)-> {
            terminate();
            diary.setLandoTerminate(System.currentTimeMillis());
        });
        counter.CountDown();
        }
    }

