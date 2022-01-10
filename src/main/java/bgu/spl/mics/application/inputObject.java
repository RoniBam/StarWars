package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;

public class inputObject {
    private Attack [] attacks;
    private long R2D2;
    private long Lando;
    private int EwoksNum;


    public Attack [] getAttacks (){ return attacks; }
    public long getLando (){return Lando;}
    public long getR2D2 (){return R2D2;}
    public int getEwoksNum (){return EwoksNum;}
}
