package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import static com.almasb.fxgl.dsl.FXGL.*;

public class JjoApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(15 * 70);
        settings.setHeight(10 * 70);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new JjoFactory());
        setLevelFromMap("try.tmx");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
