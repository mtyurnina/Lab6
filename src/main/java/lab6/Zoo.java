package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {

    private static final String CONNECT = "127.0.0.1:2181";

    private static final int TIMEOUT = 3000;

    private final ZooKeeper zooKeeper;
    private final ActorRef storeActor;

    public Zoo(ActorRef storeActor) throws IOException {
        System.out.println("zoo");
        this.zooKeeper = new ZooKeeper(CONNECT, TIMEOUT, null);
        System.out.println("zoo");
        this.storeActor = storeActor;
        System.out.println("zoo");
        serverWatch();
    }

    public void createServer(String serverURL) throws KeeperException, InterruptedException {
        System.out.println("create");
        zooKeeper.create(
                "/servers/node",
                serverURL.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
    }

    private void serverWatch() {
        try {
            System.out.println("watch");
            List<String> serverChildrenNames = zooKeeper.getChildren("/servers",
                    watchedEvent -> {
                        if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged)
                            serverWatch();
                    });
            System.out.println("watch");
            List<String> serverNames = new ArrayList<>();

            for (String string : serverChildrenNames) {
                byte[] serverURL = zooKeeper.getData("/servers" + "/" + string, null, null);
                serverNames.add(new String(serverURL));
            }
            System.out.println("watch3");
            storeActor.tell(new Message(serverNames.toArray(new String[0])), ActorRef.noSender());
        } catch (KeeperException | InterruptedException error) {
            System.out.println("catch");
            error.printStackTrace();
        }
    }
}