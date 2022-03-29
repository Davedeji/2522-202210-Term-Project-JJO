package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Initializes settings, maps, levels, and physics.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class JjoApp extends GameApplication {
    private Entity player;

    /**
     * Initializes screen settings.
     *
     * @param settings of type settings
     */
    @Override
    protected void initSettings(final GameSettings settings) {
        settings.setWidth(50 * 16);
        settings.setHeight(60 * 16);
    }

    /**
     * Initializes user's input.
     */
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }
        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }
        }, KeyCode.D, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.W, VirtualButton.UP);

        getInput().addAction(new UserAction("Stop") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.S);
    }

    /**
     * Initializes game.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new JjoFactory());
        setLevelFromMap("3.tmx");

        player = getGameWorld().spawn("player", 50, 50);

//        spawn("background");
    }

    /**
     * Initializes physics.
     */
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JjoType.PLAYER, JjoType.COIN) {
            @Override
            protected void onCollisionBegin(final Entity player, final Entity coin) {
                coin.removeFromWorld();
            }
        });
    }

    /**
     * Drives the program.
     *
     * @param args unused
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
