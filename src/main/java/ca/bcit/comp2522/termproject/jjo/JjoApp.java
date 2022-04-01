package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
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
    private static final int GRID_SIZE = 16;
    private static final int PLAYER_X = 200;
    private static final int PLAYER_Y = 500;
    private static final int GAME_WIDTH = 200;
    private static final int GAME_HEIGHT = 40;
    private Entity player;


    /**
     * Initializes screen settings.
     *
     * @param settings of type settings
     */
    @Override
    protected void initSettings(final GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
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
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.W, VirtualButton.A);

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

        spawn("background");

        setLevelFromMap("level2.tmx");

        player = getGameWorld().spawn("player", PLAYER_X, PLAYER_Y);
        // Follows character on map and adjusts screen accordingly
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 200 * 16, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
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

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JjoType.PLAYER, JjoType.ENEMY) {
            @Override
            protected void onCollisionBegin(final Entity player, final Entity enemy) {
                System.out.println("You touched enemy");
                // decrease health
                player.getComponent(PlayerComponent.class).decreaseHealth();
                getDialogService().showMessageBox("You touched enemy");
                System.out.println("Health: " + player.getComponent(PlayerComponent.class).getHealth());
                // if health is 0, game over
                if (player.getComponent(PlayerComponent.class).getHealth() == 0) {
//                    getGameController().setGameOver();
                    showGameOver();
                }
            }
        });
    }


    /**
     * Shows game over screen and prompts user to play again.
     */
    private void showGameOver() {
        getDialogService().showConfirmationBox("Game Over You died.\nPress R to restart", yes -> {
            if (yes) {
                getGameController().startNewGame();
            } else {
                getGameController().exit();
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
