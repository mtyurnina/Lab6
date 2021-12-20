package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;

import java.time.Duration;

public class Server {

    private Http http;
    private ActorRef storeActor;
    private Duration duration = Duration.ofSeconds(5);

    private void zooKeeperInitialization(int port) {
        Zoo zoo = new Zoo(storeActor);
        zoo.createServer(getServerURL());
    }
}