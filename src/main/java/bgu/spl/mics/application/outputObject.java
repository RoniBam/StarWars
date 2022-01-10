package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.atomic.AtomicInteger;

public class outputObject {

    final AtomicInteger totalAttacks;
    final long HanSoloFinish;
    final long C3P0Finish;
    final long R2D2Deactivate;
    final long LeiaTerminate;
    final long HanSoloTerminate;
    final long C3POTerminate;
    final long R2D2Terminate;
    final long LandoTerminate;

    public outputObject (Diary diary){
        totalAttacks= diary.getTotalAttacks();
        HanSoloFinish= diary.getHanSoloFinish();
        C3P0Finish= diary.getC3POFinish();
        R2D2Deactivate= diary.getR2D2Deactivate();
        LeiaTerminate=diary.getLeiaTerminate();
        HanSoloTerminate=diary.getHanSoloTerminate();
        C3POTerminate=diary.getC3POTerminate();
        R2D2Terminate= diary.getR2D2Terminate();
        LandoTerminate=diary.getLandoTerminate();
    }
}
