package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

/**
 * Creates methods to control characters movements.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class PlayerComponent extends Component {
    private static final double Y_VELOCITY = 200;
    private static final double X_VELOCITY = 150;

    private PhysicsComponent physics;

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
        physics.setVelocityY(-Y_VELOCITY);
    }
    /**
     * Stops character.
     */
    public void stop() {
        physics.setVelocityX(0);
    }

}
