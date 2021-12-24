package lab6;

import akka.actor.ActorRef;
import akka.actor.AllDeadLetters;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.Patterns;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.completeWithFuture;


public class Server extends AllDirectives{

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

    private String createURL(String url, String anonymizeUrl, int count) {
        return Uri.create(url)
                .query(Query.create(Pair.create("url", anonymizeUrl),
                        Pair.create("count", Integer.toString(count-1))
                )).toString();
    }

    private CompletionStage<HttpResponse> redirect(String url, int count) {
        return Patterns.ask(storeActor, new RandomServerMessage(), duration)
                .thenCompose(serverURL -> fetch(createURL((String) serverURL, url, count)));
    }

    public Route createRoute() {
        return get(
                () -> parameter("url", url -> parameter("count", countParameter -> {
                    int count = Integer.parseInt(countParameter);
                    return count == 0 ? completeWithFuture(fetch(url))
                            : completeWithFuture(redirect(url, count));
                }))
        );
    }
}