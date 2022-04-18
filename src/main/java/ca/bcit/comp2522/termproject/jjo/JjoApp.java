package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.ui.UI;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.showMessage;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getip;
import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.setLevelFromMap;


/**
 * Initializes settings, maps, levels, and physics.
 *
 * @author Vasily Shorin, Adedeji Toki
 * @version 2022
 */
public class JjoApp extends GameApplication {
    private static final int DEF_FONT_SIZE = 22;
    private static final int SCORE_X_OFFSET = 200;
    private static final int SCORE_Y_OFFSET = 50;
    private static final int TIMER_X_OFFSET = 200;
    private static final int TIMER_Y_OFFSET = 70;

    private static final int SCORE_INCREMENT = 20;

    private static final int GRID_SIZE = 16;
    private static final int PLAYER_SIZE = 50;
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final int NUMBER_LIVES = 3;
    private static final int MAX_SCORE = 6;
    private static final int INIT_SCORE = 0;
    private static final int INIT_TIME = 30;
    private static final int VIEWPORT_MIN_X = -1500;
    private static final int VIEWPORT_MAX_X = 200 * GRID_SIZE;
    private static final int VIEWPORT_MIN_Y = 0;
    private static final double DEF_PLAYER_X_POS = 150.0;
    private static final double DEF_PLAYER_Y_POS = 50.0;
    private double playerXPos = DEF_PLAYER_X_POS;
    private double playerYPos = DEF_PLAYER_Y_POS;
    private Entity jjoPlayer;
    private ArrayList<CoinPosition> removedEntities = new ArrayList<>();
    private JjoController uiController;


    /**
     * Initializes screen settings.
     *
     * @param settings of type settings
     */
    @Override
    protected void initSettings(final GameSettings settings) {
        settings.setWidth(SCREEN_WIDTH);
        settings.setHeight(SCREEN_HEIGHT);
        settings.setSceneFactory(new JjoSceneFactory());
        settings.setTitle("Jack Jumps");
        settings.setMainMenuEnabled(true);
    }

    /**
     * Initializes user's input.
     */
    @Override
    protected void initInput() {
        initXControls();
        initYControls();
        //fixme: change save keycode
        getInput().addAction(new UserAction("Save") {
            @Override
            protected void onActionEnd() {
                saveGame();
            }
        }, KeyCode.K);
    }
    private void initYControls() {
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionEnd() {
                jjoPlayer.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.W, VirtualButton.A);
    }
    private void initXControls() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                jjoPlayer.getComponent(PlayerComponent.class).left();
            }
        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                jjoPlayer.getComponent(PlayerComponent.class).right();
            }
        }, KeyCode.D, VirtualButton.RIGHT);
        getInput().addAction(new UserAction("Stop") {
            @Override
            protected void onAction() {
                jjoPlayer.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.S);
    }
    // loads game
    private void showLoadGame() {
        getDialogService().showConfirmationBox("Load saved game or start new game?", yes -> {
            if (yes) {
                loadSavedGame();
            }
        });
    }

    private void saveGame() {
        System.out.println("Saving");
        playerXPos = jjoPlayer.getPosition().getX();
        playerYPos = jjoPlayer.getPosition().getY();
        try {
            FileOutputStream fos = new FileOutputStream("output.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(removedEntities); // write MenuArray to ObjectOutputStream
            oos.writeObject(playerXPos);
            oos.writeObject(playerYPos);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads saved game.
     */
    private void loadSavedGame() {
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
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Initializes game variables.
     *
     * @param vars of type GameVariables
     */
    @Override
    protected void initGameVars(final Map<String, Object> vars) {
        vars.put("lives", NUMBER_LIVES);
        vars.put("score", INIT_SCORE);
        vars.put("levelTime", INIT_TIME);
    }

    /**
     * Initializes game.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new JjoFactory());
        setLevelFromMap("new.tmx");
        jjoPlayer = getGameWorld().spawn("player", PLAYER_SIZE, PLAYER_SIZE);
        set("player", jjoPlayer);
        spawn("background");

        // Follows character on map and adjusts screen accordingly
        setViewport();
        // Add timer for level
        addLevelTimer();

    }

    private void setViewport() {
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(VIEWPORT_MIN_X, VIEWPORT_MIN_Y, VIEWPORT_MAX_X, getAppHeight());
        viewport.bindToEntity(jjoPlayer, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setLazy(true);
    }

    private void addLevelTimer() {
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
                removedEntities.add(new CoinPosition(
                                        coin.getPosition().getX(), coin.getPosition().getY()));
                inc("score", 1);
                if (geti("score") == MAX_SCORE) {
                    showMessage("You win!");
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
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(
                                                    JjoType.PLAYER, JjoType.CHECKPOINT) {
            @Override
            protected void onCollisionBegin(final Entity player, final Entity checkpoint) {
                inc("levelTime", SCORE_INCREMENT);
            }
        });
    }
    /**
     * Updates character.
     *
     * @param tpf a double
     */
    @Override
    protected void onUpdate(final double tpf) {
        if (jjoPlayer.getY() > getAppHeight()) {
            getGameController().startNewGame();
        }
    }

    /**
     * Initializes UI.
     */
    @Override
    protected void initUI() {
        uiController = new JjoController(getGameScene());
        initTimerText();
        initScoreText();

        UI ui = getAssetLoader().loadUI("main.fxml", uiController);
        IntStream.range(0, geti("lives")).forEach(i -> uiController.addLife());
        getGameScene().addUI(ui);
    }

    private void initTimerText() {
        Text timerText = new Text("");
        timerText.setFont(Font.font(DEF_FONT_SIZE));
        timerText.setTranslateY(TIMER_Y_OFFSET);
        timerText.setTranslateX(getAppWidth() - TIMER_X_OFFSET);
        timerText.textProperty().bind(getip("levelTime").asString());
        addUINode(timerText);
    }

    private void initScoreText() {
        Text scoreText = new Text("");
        scoreText.setFont(Font.font(DEF_FONT_SIZE));
        scoreText.setTranslateX(getAppWidth() - SCORE_X_OFFSET);
        scoreText.setTranslateY(SCORE_Y_OFFSET);
        scoreText.textProperty().bind(getip("score").asString());
        addUINode(scoreText);
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
     * @throws ClassNotFoundException if not found
     */
    public static void main(final String[] args) throws ClassNotFoundException {
        //fixme: remove this password comment
//        AuthenticationHandler.createAccount("Vasy", "YesItIsOVer9000");
        launch(args);
    }
}
