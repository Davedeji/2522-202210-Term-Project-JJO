package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

//public class MainMenu extends FXGLMenu {
//    private static final int SIZE = 150;
//
//    public MainMenu(MenuType type) {
//        super(type);
//        System.out.println("MainMenu");
//
////        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
////        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);
//
//        Text textResume = FXGL.getUIFactoryService().newText("RESUME", Color.BLACK, FontType.TEXT, 24.0);
//        textResume.setTranslateX(50);
//        textResume.setTranslateY(100);
//        textResume.setMouseTransparent(true);
////        getContentRoot().getChildren().add(textResume);
//
//        VBox vBox = new VBox(50, textResume);
//        vBox.setTranslateX(200);
//        vBox.setTranslateY(200);
//
//        // code to customize the view of your menu
//        getContentRoot().getChildren().addAll(vBox);
//    }
//}

public class MainMenu extends FXGLMenu {

    private static final int SIZE = 150;

    private Animation<?> animation;

    public MainMenu(MenuType type) {
        super(type);
        Rectangle background = new Rectangle(FXGL.getAppWidth() + 200, FXGL.getAppHeight() + 200);
        background.setFill(Color.DARKGREEN);
        background.setX(0);
        background.setY(0);
        background.setTranslateX(-FXGL.getAppWidth() / 2.0);
        background.setTranslateY(-FXGL.getAppHeight() / 2.0);

        TextField username = new TextField("Username");
        username.setOnMouseClicked(e -> {
        username.setText("");
        });
        username.setTranslateY(100);

        TextField password = new TextField("Password");
        password.setOnMouseClicked(e -> {
        password.setText("");
        });
        password.setTranslateY(150);

        Button login = new Button("Login");
        login.setTranslateY(200);
        login.setOnAction(e -> {
            if (!username.getText().equals("") && !password.getText().equals("")) {
                boolean loginSuccessful = AuthenticationHandler.login(username.getText(), password.getText());
                if (loginSuccessful) {
                    fireNewGame();
                }
            }
        });

        Button createAccount = new Button("Create Account");
        createAccount.setTranslateY(250);
        createAccount.setOnAction(e -> {
            if (!username.getText().equals("") && !password.getText().equals("")) {
                boolean signUpSuccessful = AuthenticationHandler.createAccount(username.getText(), password.getText());
                if (signUpSuccessful) {
                    fireNewGame();
                }
            }
        });

        getContentRoot().getChildren().addAll(background, username, password, login, createAccount);

        System.out.println("MainMenu");
        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);

        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);

        animation = FXGL.animationBuilder()
                .duration(Duration.seconds(0.66))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(getContentRoot())
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .build();
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate");
        animation.setOnFinished(EmptyRunnable.INSTANCE);
        animation.stop();
        animation.start();
    }

    @Override
    protected void onUpdate(double tpf) {
//        System.out.println("onUpdate");
        animation.onUpdate(tpf);
    }


}