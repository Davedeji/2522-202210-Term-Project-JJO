package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UIController;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;

public class JjoController implements UIController {

    @FXML
    private double livesX;

    @FXML
    private double livesY;

    private List<Texture> lives = new ArrayList<>();

    private GameScene gameScene;

    /**
     * Constructor for JjoController.
     *
     * @param gameScene
     */
    public JjoController(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    /**
     * Adds a life to the lives list.
     */
    public void addLife() {
        int numLives = lives.size();
        Texture texture = getAssetLoader().loadTexture("life.png", 32, 32);
        texture.setTranslateX(livesX + 32 * numLives);
        texture.setTranslateX(livesY);

        lives.add(texture);
        gameScene.addUINode(texture);
    }

    /**
     * Removes a life from the lives list.
     */
    public void removeLife() {
        Texture texture = lives.get(lives.size() - 1);

        lives.remove(texture);

        Animation animation = getAnimationLoseLife(texture);
        animation.setOnFinished(e -> gameScene.removeUINode(texture));
        animation.play();

        Viewport viewport = gameScene.getViewport();
        Node flash = new Rectangle(viewport.getWidth(), viewport.getHeight(), Color.rgb(190, 10, 15, 0.5));

        gameScene.addUINode(flash);
        getGameTimer().runOnceAfter(() -> gameScene.removeUINode(flash), Duration.seconds(0.66));
    }

    private Animation getAnimationLoseLife(Texture texture) {
        texture.setFitWidth(64);
        texture.setFitHeight(64);

        Viewport viewport = gameScene.getViewport();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.66), texture);
        tt.setToX(viewport.getWidth() / 2 - texture.getFitWidth() / 2);
        tt.setToY(viewport.getHeight() / 2 - texture.getFitHeight() / 2);

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.66), texture);
        st.setToX(0);
        st.setToY(0);

        return new SequentialTransition(tt, st);
    }


    @Override
    public void init() {

    }
}
