package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Zoo {

    private static final String CONNECT = "127.0.0.1:2181";
    private static final int TIMEOUT = 3000;

    private ZooKeeper zooKeeper;
    private ActorRef storeActor;

    public Zoo(ActorRef storeActor) throws IOException {
        this.zooKeeper = new ZooKeeper(CONNECT, TIMEOUT, null);
        this.storeActor = storeActor;
        serverWatch();
    }

    public void createServer(String serverURL) throws KeeperException, InterruptedException {
        zooKeeper.create(
                "/servers/s",
                serverURL.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
    }

    private void serverWatch() {
        try {
            List<String> serverChildrenNames = 
        }
    }
}