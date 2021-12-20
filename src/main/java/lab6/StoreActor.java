package lab6;

import akka.actor.Props;

public class StoreActor {
    public static Props props() {
        return Props.create(StoreActor.class);
    }
}