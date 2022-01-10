package bgu.spl.mics;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    AtomicInteger counter;

    private static class SingletonHolder {
        private static Counter instance = new Counter();
    }

    public synchronized void CountDown() {
        int update = counter.decrementAndGet();
            if (update == 0) {
                this.notifyAll();
        }
    }

    private Counter() {
        counter = new AtomicInteger(4);
    }

     public synchronized AtomicInteger getValue () {
         while (counter.get() != 0) {
             try {
                 this.wait();
             } catch (InterruptedException e) { }
         }
         return counter;
     }

    public static Counter getCounter() {
        return Counter.SingletonHolder.instance;
        }

    public void setCounter () {
        counter.set(4);
        }
}
