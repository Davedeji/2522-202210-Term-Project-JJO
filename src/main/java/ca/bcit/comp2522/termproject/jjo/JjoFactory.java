package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;


/**
 * Initializes the JjoFactory with entities.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class JjoFactory implements EntityFactory {
    private static final float DEF_FRICTION = 3.0f;
    private static final int PLAYER_SIZE = 50;
    private static final int CHECKPOINT_WIDTH = 20;
    private static final int CHECKPOINT_HEIGHT = 18;
    private static final int GROUND_SENSOR_HEIGHT = 10;
    private static final int POINT_2D_5 = 5;
    private static final int POINT_2D_10 = 10;
    private static final int POINT_2D_12 = 12;
    private static final int POINT_2D_17 = 17;
    private static final int POINT_2D_25 = 25;
    private static final int BBOX_OFFSET_10 = 10;
    private static final int Z_INDEX = -500;
    private final ArrayList<Double> coinX = new ArrayList<>();
    private final ArrayList<Double> coinY = new ArrayList<>();

    /**
     * Factory class for the player.
     */
    public JjoFactory() {
        // refactor this mess
        try {
            FileInputStream fos = new FileInputStream("output.ser");
            ObjectInputStream ois = new ObjectInputStream(fos);
            ArrayList<CoinPosition> entities = (ArrayList<CoinPosition>) ois.readObject();
            entities.forEach(e -> { // for each MenuItem in MenuArray
                coinX.add(e.getX());
                coinY.add(e.getY());
            });
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Spawns background.
     *
     * @param data the data of the background.
     * @return the background.
     */
    @Spawns("background")
    public Entity newBackground(final SpawnData data) {
        return entityBuilder(data)
                .view(new ScrollingBackgroundView(texture("background/1.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .view(new ScrollingBackgroundView(texture("background/2.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .view(new ScrollingBackgroundView(texture("background/3.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .view(new ScrollingBackgroundView(texture("background/4.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .view(new ScrollingBackgroundView(texture("background/5.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .view(new ScrollingBackgroundView(texture("background/back.png").getImage(),
                        getAppWidth(), getAppHeight()))
                .zIndex(Z_INDEX)
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
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                                                    data.<Integer>get("height"))))
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
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                                                    data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    /**
     * Spawns a coin.
     *
     * @param data the data of the coin.
     * @return the coin.
     */
    @Spawns("coin")
    public Entity newCoin(final SpawnData data) {
        if (coinX.contains(data.getX()) && coinY.contains(data.getY())) {
            return entityBuilder(data).build();
        }

        double coinSize = data.<Integer>get("width");
        Image coin = new Image("coin.png", coinSize, coinSize, true, true);
        return FXGL.entityBuilder(data)
                .type(JjoType.COIN)
                .viewWithBBox(new ImageView(coin))
                .with(new CollidableComponent(true))
                .build();
    }

    /**
     * Spawns a checkpoint.
     * @param data the data of the checkpoint.
     * @return the checkpoint.
     */
    @Spawns("checkpoint")
    public Entity newCheckpoint(final SpawnData data) {
        return entityBuilder(data)
                .type(JjoType.CHECKPOINT)
                .viewWithBBox(texture("exit_sign.png", CHECKPOINT_WIDTH, CHECKPOINT_HEIGHT))
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
        physics.addGroundSensor(new HitBox(new Point2D(POINT_2D_5, PLAYER_SIZE - POINT_2D_5),
                                                BoundingShape.box(PLAYER_SIZE - BBOX_OFFSET_10,
                                                                    GROUND_SENSOR_HEIGHT)));

        physics.setFixtureDef(new FixtureDef().friction(DEF_FRICTION));
        Image character = new Image("run.gif", PLAYER_SIZE, PLAYER_SIZE, true, true);

        return entityBuilder(data)
                .type(JjoType.PLAYER)
                .viewWithBBox(new ImageView(character))
                .bbox(new HitBox(new Point2D(POINT_2D_5, POINT_2D_5),
                                 BoundingShape.circle(POINT_2D_12)))
                .bbox(new HitBox(new Point2D(POINT_2D_10, POINT_2D_25),
                                 BoundingShape.box(POINT_2D_10, POINT_2D_17)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
    }

    /**
     * Spawns an enemy.
     *
     * @param data the data of the enemy.
     * @return the enemy.
     */
    @Spawns("enemy")
    public Entity newEnemy(final SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        Image enemy = new Image("enemy.gif", PLAYER_SIZE, PLAYER_SIZE, true, true);

        return entityBuilder(data)
                .type(JjoType.ENEMY)
                .viewWithBBox(new ImageView(enemy))
//                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.circle(12)))
//                .bbox(new HitBox(new Point2D(10, 25), BoundingShape.box(10, 17)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
    }

    /**
     * Spawns health info.
     *
     * @param data the health data.
     * @return the health entity.
     */
    @Spawns("health")
    public Entity newHealthBar(final SpawnData data) {
        return entityBuilder(data)
//                .type(JjoType.HEALTH)
                .viewWithBBox(new Circle(data.<Integer>get("width") / 2.0, Color.RED))
                .with(new CollidableComponent(true))
                .build();
    }
}
