package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

/**
 * Creates methods to control characters movements.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class PlayerComponent extends Component {
    private static final double Y_VELOCITY = 400;
    private static final double X_VELOCITY = 150;
    int jumps = 3;
    int health = 3;

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel idle, running;

    private boolean isRunning = false;

    /**
     * Constructs a new PlayerComponent object.
     */
    public PlayerComponent() {
        Image image = image("run.png");
        idle = new AnimationChannel(image, 6, 96 / 6, 16, Duration.seconds(1), 1, 1);
        running = new AnimationChannel(image, 6, 96 / 6, 16, Duration.seconds(1), 0, 6);

        texture = new AnimatedTexture(idle);

        texture.loop();
    }


    public boolean isRunning() {
        return physics.getVelocityX() != 0;
    }

    public boolean isIdle() {
        return !isRunning;
    }

    public void onUpdate(Entity entity, double tpf) {
        if(isRunning()) {
//            texture.setAnimationChannel(running);
        }
    }

    @Override
    public void onAdded() {
//        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
//        entity.getViewComponent().addChild(texture);
//        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
//            if (isOnGround) {
//                jumps = 2;
//            }
//        });
        physics.onGroundProperty().addListener((o, oldValue, newValue) -> {
            if (newValue) {
                jumps = 2;
            }
        });
    }
    /**
     * Moves character left, adjusts its X axis coordinate.
     */
    public void left() {
        entity.setScaleX(-1);
        physics.setVelocityX(-X_VELOCITY);
    }
    /**
     * Moves character right, adjusts its X axis coordinate.
     */
    public void right() {
        entity.setScaleX(1);
        physics.setVelocityX(X_VELOCITY);
    }
    /**
     * Moves character up, adjusts its Y axis coordinate.
     */
    public void jump() {
        if (jumps == 0) {
            return;
        }

        physics.setVelocityY(-Y_VELOCITY);

        jumps--;
    }
    /**
     * Stops character.
     */
    public void stop() {
        physics.setVelocityX(0);
    }

}
