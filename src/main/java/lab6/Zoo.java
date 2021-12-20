package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Zoo {

    private static final String CONNECT = "127.0.0.1:2181";
    private static final int TIMEOUT = 3000;

    private ZooKeeper zooKeeper;
    private ActorRef storeActor;

    public Zoo(ActorRef storeActor) throws IOException {
        this.zooKeeper = new ZooKeeper(CONNECT, TIMEOUT, );
    }
}