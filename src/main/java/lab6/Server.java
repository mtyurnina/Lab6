package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.pattern.Patterns;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class Server {

    private Http http;
    private ActorRef storeActor;
    private Duration duration = Duration.ofSeconds(5);

    private void zooKeeperInitialization(int port) throws IOException, InterruptedException, KeeperException {
        Zoo zoo = new Zoo(storeActor);
        zoo.createServer(getServerURL(port));
    }

    public Server(final Http http, int port, ActorRef storeActor) throws IOException, InterruptedException, KeeperException {
        this.http = http;
        this.storeActor = storeActor;
        zooKeeperInitialization(port);
    }

    private String getServerURL(int port) {
        return "http://localhost:" + port;
    }

    private CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    private CompletionStage<HttpResponse> redirect(String url, int count) {
        return Patterns.ask(storeActor, new RandomServerMessage(), duration)
                .thenCompose(serverURL -> fetch(createURL))
    }

}