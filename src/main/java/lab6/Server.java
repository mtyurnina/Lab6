package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;

public class Server {

    private Http http;
    private ActorRef storeActor;
    private Duration duration = Duration.ofSeconds(5);

    private void zooKeeperInitialization(int port) throws IOException, InterruptedException, KeeperException {
        Zoo zoo = new Zoo(storeActor);
        zoo.createServer(getServerURL(port));
    }

    public Server(final Http http, int port, ActorRef storeActor) throws IOException, InterruptedException, KeeperException {
        return "http://localhost:"
    }
}