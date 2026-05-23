package edu.sdccd.cisc191;

import edu.sdccd.cisc191.Jdbcstuff.*;
import edu.sdccd.cisc191.roles.Class_role;
import edu.sdccd.cisc191.roles.Dps;
import edu.sdccd.cisc191.roles.Healer;
import edu.sdccd.cisc191.roles.Tank;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.util.Duration;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Main extends Application{


    private UserService userService;

    @Override
    public void start(Stage stage) throws SQLException {
        DatabaseInitializer.initialize();
        Connection conn = DatabaseConfig.getConnection();
        JdbcUserRepository userRepository = new JdbcUserRepository(conn);
        userService = new UserService(userRepository);

        stage.setScene(mMenu(stage));
        stage.show();


    }



    public Scene Account(Stage stage){
       stage.setTitle("create account");
        //button
        Button back = new Button("back");
        Button Confirm = new Button("Confirm");
        //labels
        Label Lname = new Label("name");
        Label Lpassword = new Label("password");

        //texts
        TextField name = new TextField();
        TextField password = new TextField();
        GridPane root = new GridPane();
        // button adds
        root.add(Confirm,1,2);
        root.add(back,2,2);
        //label adds
        root.add(Lname,1,0);
        root.add(Lpassword,2,0);
        // text adds
        root.add(name,1,1);
        root.add(password,2,1);
        // button  effects
        back.setOnAction(e-> stage.setScene(mMenu(stage))
                );
        Confirm.setOnAction(e->{

            String Uname = name.getText(); // referenced to text field on line 50
            String Password = password.getText();//referenced to line 51
            try {
                userService.createAccount(Uname, Password);
            } catch(Exception ef){
                ef.printStackTrace();
                System.out.println("account creation failed");
            }
        });
        // display stuff
        Scene scene = new Scene (root,700,700);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
        // scene reference point
   public Scene mMenu(Stage stage){
       stage.setTitle("mMenu");
        //buttons
       Button Highscore = new Button("Highscore");
        Button login = new Button("login");
        Button CreateAcc = new Button("createAcc");
        Button exit = new Button("exit");
        // button lambda
       Highscore.setOnAction(e->{
           try {
               List<Integer> scores = userService.getAllScores(); // You add this method
                //streams for mod 6
               int highScore = scores.stream()
                       .max(Integer::compareTo)
                       .orElse(0);

               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("High Score");
               alert.setContentText("Highest Score in Database: " + highScore);
               alert.showAndWait();

           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });
       login.setOnAction(e->
               stage.setScene(Login(stage)));
       CreateAcc.setOnAction(e->{
           stage.setScene(Account(stage));
       });
        //root make
        GridPane root = new GridPane();
        //add
       root.add(login,1,0);
       root.add(CreateAcc,2,0);
       root.add(exit,3,0);
       root.add(Highscore,0,0);
       //scene stuff
       Scene scene = new Scene(root,700,700);
       return scene;

   }
    public Scene Login(Stage stage){
        stage.setTitle("login");
        //button
        Button back = new Button("back");
        Button Confirm = new Button("Confirm");
        //labels
        Label Lname = new Label("name");
        Label Lpassword = new Label("password");

        //texts
        TextField name = new TextField();
        TextField password = new TextField();
        GridPane root = new GridPane();
        // button adds
        root.add(Confirm,1,2);
        root.add(back,2,2);
        //label adds
        root.add(Lname,1,0);
        root.add(Lpassword,2,0);
        // text adds
        root.add(name,1,1);
        root.add(password,2,1);
        // button  effects
        back.setOnAction(e-> {
                    stage.setScene(mMenu(stage));
                }
        );
        Confirm.setOnAction(e->{

            String Uname = name.getText(); // referenced to text field on line 50
            String Password = password.getText();//referenced to line 51
            try {
                if(userService.login(Uname, Password)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login");
                    alert.setContentText("Logged in!!!");
                    alert.showAndWait();
                    stage.setScene(class_selection(stage));
                }

            } catch(Exception ef){
                ef.printStackTrace();
                System.out.println("account creation failed");
            }
        });
        // display stuff
        Scene scene = new Scene (root,700,700);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
    public Scene class_selection(Stage stage){
        //buttons
        Button Dps = new Button("dps");
        Button Healer = new Button("Healer");
        Button Tank = new Button("tank");
        Button IDps = new Button("info");
        Button Ihealer = new Button("info");
        Button ITank = new Button("info");

        GridPane root = new GridPane();
        //add
        root.add(Dps,1,0);
        root.add(Healer,2,0);
        root.add(Tank,3,0);
        root.add(IDps,1,1);
        root.add(Ihealer,2,1);
        root.add(ITank,3,1);
        // button effects
        IDps.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Class description");
            alert.setContentText("Dps:" +
                    "+1 health"+
                    " +4 dmg" +
                    "+1 speed" );
            alert.showAndWait();

        });
        Ihealer.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Class description");
            alert.setContentText("Healer:" +
                    " +2 dmg" +
                    "+2 speed" );
            alert.showAndWait();
        });
        ITank.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Class description");
            alert.setContentText("Tank:" +
                    " +4 dmg" +
                    "+6 health" );
            alert.showAndWait();
        });
        EventHandler<ActionEvent> class_choice;
        class_choice = e->{

            Button clicked = (Button) e.getSource();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirm class choice");
            alert.setContentText("This cannot be changes");
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            alert.getButtonTypes().setAll(yes,no);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == yes ){
                //confirms class
                if(clicked == Dps){
                    PlayerStats.chosenRole = new Dps();

                }
                if(clicked == Tank){
                PlayerStats.chosenRole = new Tank();
                }
                if(clicked == Healer){
                PlayerStats.chosenRole = new Healer();
                }
                if (PlayerStats.chosenRole == null) {
                    stage.setScene(class_selection(stage));
                }
                Label countLabel = new Label();
                countLabel.setStyle("-fx-font-size: 48px;");
                countLabel.setLayoutX(300);
                countLabel.setLayoutY(300);
                Group countdownR = new Group(countLabel);
                Scene countdownScene = new Scene(countdownR,700,700);
                stage.setScene(countdownScene);
                //recursion
                countdown(3, countLabel, stage);

                Scene CountdownScene = new Scene(root,700,700);
                stage.setScene(CountdownScene);
                countdown(3,countLabel,stage);

            }
            if(result.get() == no){
            alert.close();
            }
        };
        Dps.setOnAction(class_choice);
        Tank.setOnAction(class_choice);
        Healer.setOnAction(class_choice);
        stage.setTitle("Please select your class");
        //same display stuff
        Scene scene = new Scene (root,700,700);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
    //recursion
    public void countdown(int n, Label label,Stage stage){
        if(n==0){
            stage.setScene(new GameScene(userService).createGame(stage));
            return;
        }
        label.setText(String.valueOf(n));
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e-> countdown(n-1,label,stage));
        pause.play();
    }

    public  static void main(String[] args) {
        launch(args);
    }
}