package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {

    private static final String CONNECT = "127.0.0.1:2181";
    private static final int TIMEOUT = 10000;
    private static final String PATH = "/servers";

    private final ZooKeeper zooKeeper;
    private final ActorRef storeActor;

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
            List<String> serverChildrenNames = zooKeeper.getChildren(PATH,
                    watchedEvent -> {
                        if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged)
                            serverWatch();
                    });
            List<String> serverNames = new ArrayList<>();

            for (String string : serverChildrenNames) {
                byte[] serverURL = zooKeeper.getData(PATH + "/" + string, null, null);
                serverNames.add(new String(serverURL));
            }
            storeActor.tell(new Message(serverNames.toArray(new String[0])), ActorRef.noSender());
        } catch (KeeperException | InterruptedException error) {
            error.printStackTrace();
        }
    }
}