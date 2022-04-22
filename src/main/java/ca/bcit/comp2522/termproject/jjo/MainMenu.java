package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Creates the main menu.
 * @author Vasily Shorin & Adedeji Toki
 * @version 1.0
 */
public class MainMenu extends FXGLMenu {
    private static final int SIZE = 150;
    private static final int USERNAME_Y = 100;
    private static final int PASSWORD_Y = 150;
    private static final int LOGIN_Y = 200;
    private static final int CREATE_ACCOUNT_Y = 250;
    private static final int BACKGROUND_OFFSET = 200;
    private static final double ANIMATION_DURATION = 0.66;
    private final Animation<?> animation;

    /**
     * Creates the main menu.
     * @param type the menu type.
     */
    public MainMenu(final MenuType type) {
        super(type);
        Rectangle background = addBackground();
        TextField username = addTextField("Username", USERNAME_Y);
        TextField password = addTextField("Password", PASSWORD_Y);

        Button login = new Button("Login");
        login.setTranslateY(LOGIN_Y);
        login.setOnAction(e -> {
            if (!username.getText().equals("") && !password.getText().equals("")) {
                boolean loginSuccessful = AuthenticationHandler.login(username.getText(),
                                                                        password.getText());
                if (loginSuccessful) {
                    fireNewGame();
                }
            }
        });

        Button createAccount = new Button("Create Account");
        createAccount.setTranslateY(CREATE_ACCOUNT_Y);
        createAccount.setOnAction(e -> {
            if (!username.getText().equals("") && !password.getText().equals("")) {
                boolean signUpSuccessful = AuthenticationHandler.createAccount(username.getText(),
                                                                                password.getText());
                if (signUpSuccessful) {
                    fireNewGame();
                }
            }
        });

        getContentRoot().getChildren().addAll(background, username, password, login, createAccount);
        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);
        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);

        animation = FXGL.animationBuilder()
                .duration(Duration.seconds(ANIMATION_DURATION))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(getContentRoot())
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .build();
    }

    private Rectangle addBackground() {
        Rectangle background = new Rectangle(FXGL.getAppWidth() + BACKGROUND_OFFSET,
                                            FXGL.getAppHeight() + BACKGROUND_OFFSET);
        background.setFill(Color.DARKGREEN);
        background.setX(0);
        background.setY(0);
        background.setTranslateX(-FXGL.getAppWidth() / 2.0);
        background.setTranslateY(-FXGL.getAppHeight() / 2.0);
        return background;
    }

    private TextField addTextField(final String text, final int y) {
        TextField textField = new TextField(text);
        textField.setOnMouseClicked(e -> textField.setText(""));
        textField.setTranslateY(y);
        return textField;
    }


    /**
     * Setup animation.
     */
    @Override
    public void onCreate() {
        animation.setOnFinished(EmptyRunnable.INSTANCE);
        animation.stop();
        animation.start();
    }

    /**
     * Update animation.
     */
    @Override
    protected void onUpdate(final double tpf) {
        animation.onUpdate(tpf);
    }
}
