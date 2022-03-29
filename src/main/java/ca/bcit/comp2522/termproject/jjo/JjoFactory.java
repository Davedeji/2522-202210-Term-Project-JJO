package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

/**
 * Initializes the JjoFactory with entities.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class JjoFactory implements EntityFactory {
    private static final float DEF_FRICTION = 3.0f;
    private static final int PLAYER_SIZE = 50;
    private static final int ZINDEX = -100;

    /**
     * Spawns background.
     *
     * @param data the data of the background.
     * @return the background.
     */
    @Spawns("background")
    public Entity newBackground(final SpawnData data) {
        return entityBuilder(data)
                .view(new ScrollingBackgroundView(texture("background/back.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .zIndex(ZINDEX)
                .with(new IrremovableComponent())
                .build();
    }

    /**
     * Spawns a platform.
     *
     * @param data the data of the platform.
     * @return the platform.
     */
    @Spawns("platform")
    public Entity newPlatform(final SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(JjoType.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(physics)
                .build();
    }

    /**
     * Spawns a platform Floor.
     *
     * @param data the data of the platform Floor.
     * @return the platform Floor.
     */
    @Spawns("platformFloor")
    public Entity newPlatformFloor(final SpawnData data) {

        return entityBuilder(data)
                .type(JjoType.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    /**
     * Spawns a coin.
     *
     * @param data the data of the coin.
     * @return the player.
     */
    @Spawns("coin")
    public Entity newCoin(final SpawnData data) {
        double coinSize = data.<Integer>get("width");
        Image coin = new Image("coin.png", coinSize, coinSize, true, true);
        return FXGL.entityBuilder(data)
                .type(JjoType.COIN)
                .viewWithBBox(new ImageView(coin))
                .with(new CollidableComponent(true))
                .build();
    }

    /**
     * Spawns a player.
     *
     * @param data the data of the player.
     * @return the player.
     */
    @Spawns("player")
    public Entity newPlayer(final SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setFixtureDef(new FixtureDef().friction(DEF_FRICTION));
        Image character = new Image("idle.gif", PLAYER_SIZE, PLAYER_SIZE, true, true);

        return entityBuilder(data)
                .type(JjoType.PLAYER)
                .viewWithBBox(new ImageView(character))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
}
}
