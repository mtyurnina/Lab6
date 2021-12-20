package lab6;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.Random;

public class StoreActor {
    public static Props props() {
        return Props.create(StoreActor.class);
    }

    private String getRandomServer() {
        return serverList[new Random().nextInt(severList.length())];
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> {
                    this.serverList = message.getServerList();
                })
                .match(RandomServerMessage.class, message -> sender().tell(getRandomServer(), self()))
                .build;
    }
}