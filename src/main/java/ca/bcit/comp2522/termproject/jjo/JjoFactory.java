package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

//    @Spawns("coin")
//    public Entity newCoin(SpawnData data) {
//        return FXGL.entityBuilder(data)
//                .viewFromNodeWithBBox(new Circle(data.<Integer>get("width") / 2, Color.GOLD))
//                .with(new PhysicsComponent())
//                .build();
//    }
}
