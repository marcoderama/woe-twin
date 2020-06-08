package oti.twin;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;

public class Main {
  public static Behavior<Void> create() {
    return Behaviors.setup(
        context -> {

          return Behaviors.receive(Void.class)
              .onSignal(Terminated.class, signal -> Behaviors.stopped())
              .build();
        }
    );
  }

  public static void main(String[] args) {
    ActorSystem<?> actorSystem = ActorSystem.create(Main.create(), "OTI Twin");
    ClusterSharding clusterSharding = ClusterSharding.get(actorSystem);

    clusterSharding.init(
        Entity.of(
            Region.entityTypeKey,
            entityContext ->
                Region.create(entityContext.getEntityId(), clusterSharding)
        )
    );
  }
}
