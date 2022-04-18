package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

/**
 * Initialize the JjoSceneFactory.
 * @author Vasily shorin & Adedeji Toki
 * @version 1.0
 */
public class JjoSceneFactory extends SceneFactory {
    /**
     * Creates a menu for the game.
     * @return menu
     */
    @Override
    public FXGLMenu newMainMenu() {
        System.out.println("Main Menu");
        return new MainMenu(MenuType.MAIN_MENU);
    }
}
