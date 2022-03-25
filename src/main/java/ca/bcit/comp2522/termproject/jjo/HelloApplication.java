package ca.bcit.comp2522.termproject.jjo;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static final int JUMP = 10;
    public static final int JUMP_VERTICAL = 20;

    public static final int DEFAULT_CIRCLE_X = 100;
    public static final int DEFAULT_CIRCLE_Y = 100;

//    // Declare control booleans
//    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);
//    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
//    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
//    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
//
//    final BooleanBinding leftUpPressed = leftPressed.and(upPressed);
//    final BooleanBinding rightUpPressed = rightPressed.and(upPressed);
//    final BooleanBinding leftDownPressed = leftPressed.and(downPressed);
//    final BooleanBinding rightDownPressed = rightPressed.and(downPressed);






//    @FXML
    private Circle myCircle;
//    private double x;
//    private double y;

    @Override
    public void start(Stage stage) throws IOException {

        myCircle = new Circle(DEFAULT_CIRCLE_X, DEFAULT_CIRCLE_Y, 50);
        myCircle.setFill(Color.BLUE);

        Group root = new Group(myCircle);


        final int appWidth = 800;
        final int appHeight = 600;
        Scene scene = new Scene(root, appWidth, appHeight, Color.FORESTGREEN);



        Text gameNameTxt = new Text();
        gameNameTxt.setText("Jack Jumps");
        gameNameTxt.setFont(Font.font("verdana", 50));
        gameNameTxt.setFill(Color.LIGHTGRAY);
        gameNameTxt.setX(0 - gameNameTxt.getLayoutBounds().getWidth() / 2);
        gameNameTxt.setY(100);
        gameNameTxt.translateXProperty().bind(scene.widthProperty().divide(2));


//        leftUpPressed.addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//                System.out.println("Left up pressed");
//            }
//        });

//        Rectangle rectangle = new Rectangle(scene.getWidth(), scene.getHeight());
//        rectangle.setX(100);
//        rectangle.setY(100);
//        rectangle.setFill(Color.BLACK);
//        rectangle.setWidth(200);
//        rectangle.setHeight(200);
//        root.getChildren().add(rectangle);




//        root.getChildren().add(gameNameTxt);
//
//        Parent sceneRoot = FXMLLoader.load(getClass().getResource("main.fxml"));
//
//
//        root.getChildren().add(myCircle);

        scene.setOnKeyPressed(this::processKeyPress);
        scene.setOnKeyReleased(this::processKeyReleased);

        stage.setTitle("Jack Jumps");
//        stage.setScene(new Scene(sceneRoot, 600, 600));
        stage.setScene(scene);
        stage.show();


    }

    /**
     * Modifies the position of the image view when an arrow key is pressed.
     *
     * @param event invoked this method
     */
    public void processKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                myCircle.setCenterY(myCircle.getCenterY() - JUMP_VERTICAL);;
                break;
            case DOWN:
                myCircle.setCenterY(myCircle.getCenterY() + JUMP_VERTICAL);
                break;
            case RIGHT:
                myCircle.setCenterX(myCircle.getCenterX() + JUMP);
                break;
            case LEFT:
                myCircle.setCenterX(myCircle.getCenterX() - JUMP);
                break;
            default:
                break; // Does nothing if it's not an arrow key
        }
    }

    /**
     * Modifies the position of the image view when an arrow key is pressed.
     *
     * @param event invoked this method
     */
    public void processKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                myCircle.setCenterY(DEFAULT_CIRCLE_Y);
                break;
            case DOWN:
                myCircle.setCenterY(DEFAULT_CIRCLE_Y);
            default:
                break; // Does nothing if it's not an arrow key
        }
    }

    public static void main(String[] args) {
        launch();
    }
}