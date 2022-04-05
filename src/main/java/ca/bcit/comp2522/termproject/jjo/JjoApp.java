package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.ui.UI;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Initializes settings, maps, levels, and physics.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class JjoApp extends GameApplication {
    private static final int GRID_SIZE = 16;
    private static final int PLAYER_SIZE = 50;
    private static final int GAME_WIDTH = 50;
    private static final int GAME_HEIGHT = 60;
    private Entity player;
    private double playerXPos = 150.0;
    private double playerYPos = 50.0;
    private ArrayList<CoinPosition> removedEntities = new ArrayList<>();
    private JjoController uiController;
//    private ArrayList<CoinPosition> copyEntities = new ArrayList<>();


    /**
     * Initializes screen settings.
     *
     * @param settings of type settings
     */
    @Override
    protected void initSettings(final GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        System.out.println("init SceneFactory");
        settings.setSceneFactory(new JjoSceneFactory());
        System.out.println("Done SceneFactory");
        settings.setTitle("Jack Jumps");
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
        getInput().addAction(new UserAction("Save") {
            @Override
            protected void onActionEnd() {
                System.out.println("Saving");
                playerXPos = player.getPosition().getX();
                playerYPos = player.getPosition().getY();
                try {
                    FileOutputStream fos = new FileOutputStream("output.ser");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(removedEntities); // write MenuArray to ObjectOutputStream
                    oos.writeObject(playerXPos);
                    oos.writeObject(playerYPos);
                    oos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, KeyCode.K);
    }

    private void showLoadGame() {
//        File file = new File("output.ser");
//        if (!file.exists()) {
//            return;
//        }
        getDialogService().showConfirmationBox("Load saved game or start new game?", yes -> {
            if (yes) {
                loadSavedGame();
            } else {
                return;
            }

        });
    }

    protected void loadSavedGame() {
        System.out.println("Loading");
        try {
            FileInputStream fos = new FileInputStream("output.ser");
            ObjectInputStream ois = new ObjectInputStream(fos);
            removedEntities = (ArrayList<CoinPosition>) ois.readObject();
            playerXPos = (Double) ois.readObject();
            playerYPos = (Double) ois.readObject();
            System.out.println("Player X: " + playerXPos);
            System.out.println("Player Y: " + playerYPos);
            ois.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("lives", 3);
    }

    /**
     * Initializes game.
     */
    @Override
    protected void initGame() {
//        FXGl.getGameState().
        // Load saved game
//        showLoadGame();
//        loadSavedGame();
        System.out.println("init Game");
        getGameWorld().addEntityFactory(new JjoFactory());
        spawn("background");

        setLevelFromMap("level2.tmx");

        player = getGameWorld().spawn("player", playerXPos, playerYPos);
        // Follows character on map and adjusts screen accordingly
        Viewport viewport = getGameScene().getViewport();
//        Viewport viewport = get().getViewport();
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
                removedEntities.add(new CoinPosition(coin.getPosition().getX(), coin.getPosition().getY()));
                coin.removeFromWorld();
                uiController.addLife();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JjoType.PLAYER, JjoType.ENEMY) {
            @Override
            protected void onCollisionBegin(final Entity player, final Entity enemy) {
                System.out.println("You touched enemy");
                inc("lives", -1);
                uiController.removeLife();
                // if health is 0, game over
                if (geti("lives")==0) {

                    showGameOver();
                }
            }
        });
    }

    @Override
    protected void initUI() {
        uiController = new JjoController(getGameScene());

        UI ui = getAssetLoader().loadUI("main.fxml", uiController);

        IntStream.range(0, geti("lives")).forEach(i -> uiController.addLife());

        getGameScene().addUI(ui);
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
