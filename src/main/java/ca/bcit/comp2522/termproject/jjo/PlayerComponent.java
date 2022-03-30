package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

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

    private PhysicsComponent physics;

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
            System.out.println(newValue ? "On Ground" : "In the air");
            if (newValue) {
                jumps = 3;
            }
        });
    }
    /**
     * Moves character left, adjusts its X axis coordinate.
     */
    public void left() {
        physics.setVelocityX(-X_VELOCITY);
    }
    /**
     * Moves character right, adjusts its X axis coordinate.
     */
    public void right() {
        physics.setVelocityX(X_VELOCITY);
    }
    /**
     * Moves character up, adjusts its Y axis coordinate.
     */
    public void jump() {
        if (jumps == 0) {
//            System.out.println("You can't jump anymore!");
            return;
        }

        physics.setVelocityY(-Y_VELOCITY);

        jumps--;
        System.out.println("jumps: " + jumps);
    }
    /**
     * Stops character.
     */
    public void stop() {
        physics.setVelocityX(0);
    }

}
