package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {
	final HashMap<Class<? extends Message>, LinkedBlockingDeque<MicroService>> messages;
	final HashMap<MicroService, LinkedBlockingQueue<Message>> MS;
	final HashMap<Event, Future> futures;


	private static class SingletonHolder {
		final static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		messages = new HashMap();
		MS = new HashMap();
		futures = new HashMap();
	}

	public static MessageBusImpl getMessageBus() {
		return SingletonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subScribeMessage(type, m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		subScribeMessage(type, m);
	}

	private synchronized void subScribeMessage(Class<? extends Message> type, MicroService m) {
		if (messages.containsKey(type)) {
			LinkedBlockingDeque<MicroService> toSubscribe = messages.get(type);
			toSubscribe.add(m);
			}
		else {
			LinkedBlockingDeque<MicroService> toSubscribe = new LinkedBlockingDeque<>();
			toSubscribe.add(m);
			messages.put(type, toSubscribe);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> toResolve = futures.get(e);
		synchronized (this) {
			toResolve.resolve(result);
			notifyAll();
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		LinkedBlockingDeque<MicroService> toSend = messages.get((b.getClass()));
		synchronized (toSend) {
			for (MicroService element : toSend) {
				LinkedBlockingQueue currentMS = MS.get(element);
				currentMS.add(b);
			}
		}
	}

	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		// sync with send Event
		//in orded to keep round robin manner, i pull out the first MS, add a message to its queue and push it back to the end of the message queue
		if (messages.get(e.getClass()) != null && messages.get(e.getClass()).size() != 0) {

			MicroService next = messages.get(e.getClass()).pollFirst();
			MS.get(next).add(e);
			Future<T> result = new Future<>();
			futures.put(e, result);
			messages.get(e.getClass()).addLast(next);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> toAdd = new LinkedBlockingQueue<>();
		synchronized (this) {
			MS.put(m, toAdd);
		}
	}

	@Override
	public void unregister(MicroService m) {
		Set<Class<? extends Message>> keys = messages.keySet();
		for (Class<? extends Message> element : keys) {
			LinkedBlockingDeque<MicroService> current = messages.get(element);
			for (MicroService mic : current) {
				if (mic.equals(m)) {
					current.remove(m);
				}
			}
		}
		if (MS.containsKey(m)) {
			MS.remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		try {
			if (MS.containsKey(m)) {
				Message toReturn = MS.get(m).take();
				return toReturn;
			} else {
				throw new IllegalStateException("this microservice is not registered");
			}
		} catch (InterruptedException e) {
			throw e;
		}
	}
}
