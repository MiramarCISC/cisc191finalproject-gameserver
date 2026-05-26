# Paddle Score Game

## Overview
This project is for the CISC191 Final. It utilizes the various modules from 1 to 7 in order to create a mockup twist of Pong. The goal is to get the highest score, of course. You can store a high score and etc via an account with a username and password.

### Instructions for running the project:

- Run `mvn javafx:run` in Maven

### Instructions for running the tests:

- Run `mvn test` in Maven from the root folder or run individually in `src/test/java/edu/sdccd/cisc191/`

## Demonstration of topics/modules

| Module Topic                                            | Feature/Purpose                                                                                                                             | Code File Path + Line Numbers                                                  | Unit Test File Path + Line Numbers                   |
|---------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|------------------------------------------------------|
| Module 1: Arrays + OO Refresh                           | Arrays for the highscores to be saved somewhere. The design is in our main for our menu in order to have easy scene switches.               | `src/main/java/edu/sdccd/cisc191/PlayerStats.java`, line 15                    | `src/test/java/edu/sdccd/cisc191/Module1N6Test.java` |
| Module 2: OO Design + Functional Interfaces             | The `UserRepository` is used for the methods that are needed for our password code to work which checks login.                              | `src/main/java/edu/sdccd/cisc191/Jdbcstuff/UserRepository.java`, lines [14-25] | `src/test/java/edu/sdccd/cisc191/Module2N4Test.java` |
| Module 3: Inheritance + Polymorphism                    | These subclasses override the abstract parent class methods and variables in order to pass in their own stats specific for that class type. | Entirety of all classes inside `src/main/java/edu/sdccd/cisc191/roles`         | `src/test/java/edu/sdccd/cisc191/Module3Test.java`   |
| Module 4: Exceptions + File I/O + Database Persistence  | All JDBC aspects, such as the stored passwords and usernames and high score for each user.                                                  | Entirety of all classes inside `src/main/java/edu/sdccd/cisc191/Jdbcstuff`     | `src/test/java/edu/sdccd/cisc191/Module2N4Test.java` |
| Module 5: Recursion + Algorithms                        | The recursive function serves as the a countdown to zero before the game starts.                                                            | `src/main/java/edu/sdccd/cisc191/Main.java`, lines [281 - 290]                 | `src/test/java/edu/sdccd/cisc191/Module5N7Test.java` |
| Module 6: Collections + Generics + Advanced Streams     | This saves the current score into a history list finding the highest score ever recorded.                                                   | `src/main/java/edu/sdccd/cisc191/GameScene.java`, lines [120 - 126]            | `src/test/java/edu/sdccd/cisc191/Module1N6Test.java` |
| Module 7: JavaFX + Events + Lambdas                     | The JavaFX code takes in user input events and figures out what class the player chose.                                                     | `src/main/java/edu/sdccd/cisc191/Main.java`, lines [226 - 250]                 | `src/test/java/edu/sdccd/cisc191/Module5N7Test.java` |

## Reflection
### What I am most proud of:
I'm most proud of keeping myself conscious while working on this, while being able to combine multiple of the modules together into multiple methods. I also was happy to figure out how to condense the tests, following this logic.
### What I would improve with more time:
Given more time, I could probably improve the GUI greatly and improve the physics functionality. Additionally, keeping code cleaner and more separated.