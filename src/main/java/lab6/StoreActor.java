package lab6;

import akka.actor.Props;

import java.util.Random;

public class StoreActor {
    public static Props props() {
        return Props.create(StoreActor.class);
    }

    private String getRandomServer() {
        return serverList[new Random().nextInt()]
    }
}