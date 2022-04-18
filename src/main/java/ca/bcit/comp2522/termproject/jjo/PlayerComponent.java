package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
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
    private static final int FRAMES_PER_ROW = 6;
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;
    private static final int JUMP_INITIALIZER = 2;
    private static int jumps = JUMP_INITIALIZER;
//    private static int health = 3;

    private PhysicsComponent physics;

    /**
     * Constructs a new PlayerComponent object.
     */
    public PlayerComponent() {
        Image image = image("run.png");
        AnimationChannel idle = new AnimationChannel(image, FRAMES_PER_ROW, FRAME_WIDTH,
                FRAME_HEIGHT, Duration.seconds(1), 1, 1);
        AnimatedTexture texture = new AnimatedTexture(idle);
        texture.loop();
    }
    /**
     * Adds jumps to character.
     */
    @Override
    public void onAdded() {
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
