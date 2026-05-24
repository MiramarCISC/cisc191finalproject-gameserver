package edu.sdccd.cisc191;

import edu.sdccd.cisc191.Jdbcstuff.UserRepository;
import edu.sdccd.cisc191.Jdbcstuff.UserService;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;

import static edu.sdccd.cisc191.PlayerStats.cScore;
public class GameScene {
    private final UserService userService;

    public GameScene(UserService userService) {
        this.userService = userService;
    }
    Label Score = new Label("score: 0");
    private Rectangle paddle;
    private Circle circle;

    private double paddleSpeed;
    private double[] ballSpeed = {5};
    private double[] direction = new double[1];

    public Scene createGame(Stage stage) {


        Score.setLayoutX(10);
        Score.setLayoutY(10);

        // Use chosen class speed (replaces old PlayerStats.speed())
        paddleSpeed = PlayerStats.chosenRole.speed();

        // Paddle (kept original size + position)
        paddle = new Rectangle(40, 10);
        paddle.setLayoutX(200);
        paddle.setLayoutY(420);

        // Ball (kept original size + position)
        circle = new Circle(10);
        circle.setLayoutX(250);
        circle.setLayoutY(250);

        // Original random direction logic
        Random num = new Random();
        direction[0] = Math.toRadians(num.nextInt(360));
        if (direction[0] == Math.toRadians(180)) {
            direction[0] = Math.toRadians(num.nextInt(360));
        }

        Group root = new Group(Score, paddle, circle);
        Scene scene = new Scene(root, 500, 500);

        startBallMovement();
        handleInput(scene);

        return scene;
    }

    private void startBallMovement() {
        AnimationTimer ballM = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //move logic
                double dx = Math.cos(direction[0]) * ballSpeed[0];
                double dy = Math.sin(direction[0]) * ballSpeed[0];

                circle.setLayoutX(circle.getLayoutX() + dx / 2);
                circle.setLayoutY(circle.getLayoutY() + dy / 2);

                // wall bounce
                if (circle.getLayoutX() <= 20 || circle.getLayoutX() >= 480) {
                    direction[0] = Math.PI - direction[0];
                    ballSpeed[0]+=0.2;
                    if (ballSpeed[0] > 10) {
                        ballSpeed[0] = 10;
                    }
                }

                if (circle.getLayoutY() <= 20) {
                    direction[0] = -direction[0];
                    ballSpeed[0]+=0.2;
                    if (ballSpeed[0] > 10) {
                        ballSpeed[0] = 10;
                    }
                }

                // reset
                if (circle.getLayoutY() >= 480) {
                    circle.setLayoutX(250);
                    circle.setLayoutY(250);
                    try {
                        userService.saveScore(PlayerStats.cScore);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    PlayerStats.scoreHistory.add(cScore);
                    PlayerStats.cScore = 0;
                    Score.setText("Score: 0");
                    ballSpeed[0] = 5;
                }

                //collsion
                if (circle.getBoundsInParent().intersects(paddle.getBoundsInParent())) {

                    direction[0] = -direction[0];
                    circle.setLayoutY(circle.getLayoutY() - 5);
                    direction[0] += (Math.random() - 0.5) * 0.5;
                    cScore++;

                    Score.setText("Score: " + cScore);
                //  comperator work for module 3
                    PlayerStats.scoreHistory.add(PlayerStats.cScore);
                    int highScore = PlayerStats.scoreHistory.stream()
                            .max(Integer::compareTo)
                            .orElse(0);


                }
            }
        };
        ballM.start();
    }

    private void handleInput(Scene scene) {
        scene.setOnKeyPressed(event -> {

            // Original A/D movement logic
            if (event.getCode() == KeyCode.A) {
                paddle.setLayoutX(paddle.getLayoutX() - paddleSpeed);
                if (paddle.getLayoutX() <= 0) {
                    paddle.setLayoutX(0);
                }
            }

            if (event.getCode() == KeyCode.D) {
                paddle.setLayoutX(paddle.getLayoutX() + paddleSpeed);
                if (paddle.getLayoutX() >= 460) {
                    paddle.setLayoutX(460);
                }
            }
        });
    }
}
