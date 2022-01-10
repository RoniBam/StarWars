package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;


public class AttackEvent<Boolean> implements Event<Boolean> {
	final int [] ewoksForAttack;
	final long attackDuration;

	public AttackEvent (Attack info){
	  ewoksForAttack= new int [info.getAttackList().size()];
	  attackDuration=info.getAttackDuration();
    }

	public int [] getAttackList (){
		return ewoksForAttack;
	}

	public long getAttackDuration (){
		return attackDuration;
	}
}
