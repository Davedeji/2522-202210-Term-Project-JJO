package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

public class JjoSceneFactory extends SceneFactory {


    @Override
    public FXGLMenu newMainMenu() {
        System.out.println("Main Menu");
        return new MainMenu(MenuType.MAIN_MENU);
    }

//    @Override
//    public FXGLMenu newGameMenu() {
//        return new MainMenu(MenuType.GAME_MENU);
//    }
}
