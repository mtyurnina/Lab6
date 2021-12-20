package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Zoo {
    private ZooKeeper zooKeeper;
    private ActorRef storeActor;

    public Zoo(ActorRef storeActor) throws IOException {
        this.zooKeeper = new ZooKeeper();
    }
}