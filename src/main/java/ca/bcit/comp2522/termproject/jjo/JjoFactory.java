package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class JjoFactory implements EntityFactory {
    /**
     * Spawns a platform.
     *
     * @param data the data of the platform.
     * @return the platform.
     */
    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
                .type(JjoType.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(JjoType.COIN)
                .viewWithBBox(new Circle(data.<Integer>get("width") / 2, Color.GOLD))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(JjoType.PLAYER)
                .viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
}
}
