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

    private PhysicsComponent physics;

//    @Override
    public void onUpdate(final Entity entity, final double tpf) {

    }

    /**
     * Moves character left, adjusts its X axis coordinate.
     */
    public void left() {
        physics.setVelocityX(-150);
    }
    /**
     * Moves character right, adjusts its X axis coordinate.
     */
    public void right() {
        physics.setVelocityX(150);
    }
    /**
     * Moves character up, adjusts its Y axis coordinate.
     */
    public void jump() {
        physics.setVelocityY(-400);
    }

}
