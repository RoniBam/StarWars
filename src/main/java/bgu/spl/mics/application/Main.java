package bgu.spl.mics.application;

import bgu.spl.mics.Counter;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static bgu.spl.mics.application.passiveObjects.Diary.getDiary;
import static bgu.spl.mics.application.passiveObjects.Ewoks.getEwoks;


/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length != 2) {
			System.out.println(args.length);
			throw new IllegalArgumentException("wrong number of input argumants");
		} else {
			final String inputPath = args[0];
			Gson input = new Gson();
			inputObject InputObject = input.fromJson(new FileReader(inputPath), inputObject.class);
			LeiaMicroservice leia = new LeiaMicroservice(InputObject.getAttacks());
			Thread leiaThread = new Thread(leia);
			for (int i = 1; i <= InputObject.getEwoksNum(); i++) {
				Ewok ewok = new Ewok(i);
				Ewoks ewoks = getEwoks();
				ewoks.addEwok(ewok);
			}
			MessageBusImpl bus=MessageBusImpl.getMessageBus();
			Counter counter=Counter.getCounter();
			Diary diary = getDiary();
			HanSoloMicroservice hanSolo = new HanSoloMicroservice();
			Thread hanSoloThread = new Thread(hanSolo);
			C3POMicroservice C3PO = new C3POMicroservice();
			Thread C3POThread = new Thread(C3PO);
			R2D2Microservice R2D2 = new R2D2Microservice(InputObject.getR2D2());
			Thread R2D2Thread = new Thread(R2D2);
			LandoMicroservice Lando = new LandoMicroservice(InputObject.getLando());
			Thread LandoThread = new Thread(Lando);
			leiaThread.start();
			hanSoloThread.start();
			C3POThread.start();
			R2D2Thread.start();
			LandoThread.start();

			//waits for thread to end

			leiaThread.join();
			hanSoloThread.join();
			C3POThread.join();
			R2D2Thread.join();
			LandoThread.join();

			Gson output = new Gson();
			outputObject toJson = new outputObject(diary);
			FileWriter writer = new FileWriter(args[1]);
			output.toJson(toJson, writer);
			writer.close();
			counter.setCounter();
		}
	}
}



