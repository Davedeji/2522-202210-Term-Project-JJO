package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UIController;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;

/**
 * Controls the Jjo game.
 * @author adedejitoki & vasilyshorin
 * @version 1.0
 */
public class JjoController implements UIController {

    private static final int TEXTURE_DIMENSION_32 = 32;
    private static final int TEXTURE_DIMENSION_64 = 64;
    private static final Color LIFE_COLOR = Color.rgb(190, 10, 15, 0.5);
    private static final double ANIMATION_DURATION = 0.66;
    private final List<Texture> lives = new ArrayList<>();
    private final GameScene gameScene;

    /**
     * Constructor for JjoController.
     * @param gameScene the game scene.
     */
    public JjoController(final GameScene gameScene) {
        this.gameScene = gameScene;
    }

    /**
     * Adds a life to the lives list.
     */
    public void addLife() {
        Texture texture = getAssetLoader().loadTexture(
                "life.png", TEXTURE_DIMENSION_32, TEXTURE_DIMENSION_32);

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
        Node flash = new Rectangle(viewport.getWidth(), viewport.getHeight(), LIFE_COLOR);

        gameScene.addUINode(flash);
        getGameTimer().runOnceAfter(() -> gameScene.removeUINode(flash),
                Duration.seconds(ANIMATION_DURATION));
    }

    private Animation getAnimationLoseLife(final Texture texture) {
        texture.setFitWidth(TEXTURE_DIMENSION_64);
        texture.setFitHeight(TEXTURE_DIMENSION_64);

        Viewport viewport = gameScene.getViewport();

        TranslateTransition tt = new TranslateTransition(
                                        Duration.seconds(ANIMATION_DURATION), texture);
        tt.setToX(viewport.getWidth() / 2 - texture.getFitWidth() / 2);
        tt.setToY(viewport.getHeight() / 2 - texture.getFitHeight() / 2);

        ScaleTransition st = new ScaleTransition(Duration.seconds(ANIMATION_DURATION), texture);
        st.setToX(0);
        st.setToY(0);

        return new SequentialTransition(tt, st);
    }


    @Override
    public void init() {

    }
}
