package lab6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class PseudoAnonymizeApp {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        System.out.println("start!");
        ActorSystem actorSystem = ActorSystem.create("routes");
        ActorRef storeActor = actorSystem.actorOf(StoreActor.props(), "storeActor");

        final Http http = Http.get(actorSystem);
        final ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);

        Server server = new Server(http, PORT, storeActor);

        final Flow<HttpRequest, HttpResponse, NotUsed>
                routeFlow = server.createRoute().flow(actorSystem, actorMaterializer);

        final CompletionStage<ServerBinding> bindingCompletionStage = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                actorMaterializer
        );


        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();

        bindingCompletionStage
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> actorSystem.terminate());
    }
}