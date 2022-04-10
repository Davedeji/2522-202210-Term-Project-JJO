package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.ui.UI;
import com.almasb.fxgl.entity.level.Level;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
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
    private static final int STARTING_LEVEL = 0;
    private static final int MAX_SCORE = 6;
    private static final int MAX_LEVEL = 3;
    private double playerXPos = 150.0;
    private double playerYPos = 50.0;
    private ArrayList<CoinPosition> removedEntities = new ArrayList<>();
    private JjoController uiController;
    private PlayerComponent playerComponent;
    //    private ArrayList<CoinPosition> copyEntities = new ArrayList<>();
    private IntegerProperty countdown = new SimpleIntegerProperty(0);
    private BooleanBinding isCountdownGreaterZero = countdown.greaterThan(0);
    private BooleanProperty timerCondition = new SimpleBooleanProperty();


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
//        settings.setMainMenuEnabled(true);
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
    // loads game
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

    /**
     * Loads saved game.
     */
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

    /**
     * Initializes game variables
     *
     * @param vars of type GameVariables
     */
    @Override
    protected void initGameVars(final Map<String, Object> vars) {
        vars.put("lives", 3);
        vars.put("level", STARTING_LEVEL);
        vars.put("score", 0);
        vars.put("levelTime", 30);
    }

    /**
     * Initializes game.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new JjoFactory());
//        FXGl.getGameState().
        // Load saved game
//        showLoadGame();
//        loadSavedGame();
        System.out.println("init Game");
        setLevelFromMap("new.tmx");
        player = null;
        nextLevel();
        player = getGameWorld().spawn("player", 50, 50);
        set("player", player);
        spawn("background");
//        player = getGameWorld().spawn("player", playerXPos, playerYPos);
        // Follows character on map and adjusts screen accordingly
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 200 * 16, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);

        getGameTimer().runAtInterval(() -> {
            inc("levelTime", -1);
            if (geti("levelTime") == 0) {
                showGameOver();
            }
        }, Duration.seconds(1));

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
                inc("score", 1);
                if (geti("score") == MAX_SCORE) {
                    nextLevel();
                }
                coin.removeFromWorld();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JjoType.PLAYER, JjoType.ENEMY) {
            @Override
            protected void onCollisionBegin(final Entity player, final Entity enemy) {
                inc("lives", -1);
                uiController.removeLife();
                // if health is 0, game over
                if (geti("lives") == 0) {
                    showGameOver();
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JjoType.PLAYER, JjoType.CHECKPOINT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity checkpoint) {
                inc("levelTime", 20);
            }
        });
    }


    private void nextLevel() {
        inc("level", +1);

        if (geti("level") == MAX_LEVEL) {
            showMessage("You win!");
            return;
        }
        setLevel(geti("level"));
    }

    /**
     * Updates character.
     *
     * @param tpf a double
     */
    @Override
    protected void onUpdate(final double tpf) {
        if (player.getY() > getAppHeight()) {
            getGameController().startNewGame();
        }
    }

    private void onPlayerDied() {
        setLevelFromMap("main.tmx");
    }


    @Override
    protected void initUI() {
        uiController = new JjoController(getGameScene());
        Text uiScore = new Text("");
        Text timer = new Text("");
        timer.setFont(Font.font(22));
        timer.setTranslateY(70);
        timer.setTranslateX(getAppWidth() - 200);
        timer.textProperty().bind(getip("levelTime").asString());

        uiScore.setFont(Font.font(22));
        uiScore.setTranslateX(getAppWidth() - 200);
        uiScore.setTranslateY(50);
//        uiScore.fillProperty().bind(getop(d"stageColor"));
        uiScore.textProperty().bind(getip("score").asString());
        addUINode(timer);
        addUINode(uiScore);

        UI ui = getAssetLoader().loadUI("main.fxml", uiController);

        IntStream.range(0, geti("lives")).forEach(i -> uiController.addLife());

        getGameScene().addUI(ui);
    }

    private void setLevel(final int levelNumber) {
//        if (player != null) {
//            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
//            player.setZIndex(Integer.MAX_VALUE);
//        }
//
//        getGameWorld().getEntitiesCopy().forEach(e -> e.removeFromWorld());
//        setLevelFromMap("level" + levelNumber + ".tmx");
//
//        spawn("player", 50, 50);

    }

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
    public static void main(final String[] args) throws ClassNotFoundException {
//        boolean loggedIn = AuthenticationHandler.login("Vasily", "YesItIsOVer9000");
//        if (loggedIn) {
//            launch(args);
//        }
//        AuthenticationHandler.createAccount("Vasy", "YesItIsOVer9000");
        launch(args);
    }
}
