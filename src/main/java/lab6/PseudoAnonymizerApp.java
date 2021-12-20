package lab6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

public class PseudoAnonymizerApp {

    public static void main(String[] args) throws IOException {
        System.out.println("start!");
        ActorSystem actorSystem = ActorSystem.create("routes");
        ActorRef storeActor = actorSystem.actorOf(StoreActor.props(), "storeActor");

    }
}