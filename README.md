# Space Rally

This is a Java implementation of a multiplayer car racing game where players control their cars by typing the correct text to move forward on the race track. Players compete to be the first to complete the race and reach the finish line.

## Requirements
* Java 21
* Java Swing

## Installation

### Dependencies
Ensure you have the following dependencies installed:
- Java 17 or higher
- Gradle 6.4 or higher
- An IDE (such as IntelliJ IDEA, Eclipse, or VS Code)

### Clone the Repository
First, clone the repository to your local machine:

```bash
git clone https://gitlab2.cip.ifi.lmu.de/sosy-lab/sep-ss-2024/a5-g13.git
cd a5-g13
```

## How to Run the Game
### Using Command Line
1. **Run the GameServer**:
   
This command starts the game server which manages the game state and player connections.
```bash
./gradlew runServer
```
Wait for the command line to display the prompt ‘Server started, listening...
Waiting for clients... ’, then proceed to the next step.
2. **Run the GUI**:

This command starts the GUI allowing players to join and play the game.

```bash
./gradlew runGui
```
- If the host wants to be both a server and a player, the host needs to open a new Terminal in this file directory to execute the command to run the GUI.


### Using an IDE

1. **Open the Project**:

- Launch your preferred IDE (such as IntelliJ IDEA, Eclipse, or VS Code).
- Open the cloned project directory (a5-g13).

2. **Build the Project**:

- Your IDE should automatically recognize the Gradle project and start syncing dependencies. If it doesn't, you can manually refresh the Gradle project (usually there's a "Refresh" button in the Gradle tool window).

3. **Run the GameServer**:

- Navigate to the com.game.typeracer.server.GameServer class in the src directory.
- Right-click on the class file and select Run 'GameServer.main()' or a similar option in your IDE.

4. **Run the GUI**:

- Open a new IDE run configuration within the IDE.
- Navigate to the com.game.typeracer.view.Gui class in the src directory.
- Right-click on the class file and select Run 'GUI.main()' or a similar option in your IDE.

### Notes
- Please note that there must be one game server in a game, and the player running the GameServer is the host of this game.
- Ensure the GameServer is running before starting the GUI to ensure proper connectivity.
- If you encounter any issues, try rebuilding the project using your IDE's build tools or by running gradle clean build in the terminal.
- For further information or troubleshooting, refer to the Gradle documentation or the project documentation.


## Controls
The game is controlled using a simple graphical user interface (GUI). Here are the basic controls:

* **Mouse Clicks**: Navigate through menus and select options.
* **Text Input**: Type in the text field to control your car's speed.

* **Buttons**:
    - Click "Join New Game" to enter the lobby where you can prepare for the game.
    - Click "Start Game" to begin the race.
    - Click "Exit" to leave the game.

## How to Play
The player who completes the race first and reaches the finish line is declared the winner.

### Setup
1. Host: run the 'GameServer'.
2. Guest: run the 'GUI'.
3. Enter your name in the provided text field.
4. As a guest: Enter the IPv4-address of the host
   - As a host: Leave the server-address blanc
5. Join a game or wait for others to join.
6. As a host: click "Start Game" once all players are ready.
   - As a guest: wait for the host to start the game.


### Game Play

- Players receive random phrases to type.
- Each correct word accelerates your car along the track.
- First player to reach the end of the track wins.

## Troubleshooting
If you encounter any issues while installing or playing the game, try the following solutions:

- Make sure you have the correct version of Java installed.
- If the game is running slowly or not responding, try closing other applications to free up system resources.

If you're still having trouble, please contact us.

## Contribution
This project is not currently accepting contributions. Please check back in the future for any changes to this policy.

## Acknowledgements
This project is inspired by classic typing games and modern racing simulations. Special thanks to all contributors and testers who helped bring "Space Rally" to life.

Team Members: 
      
        - Ugur Yavuz Dogan: uguryavuzdogan@gmail.com
        - Yili Li: Yil.Li@campus.lmu.de
        - Yuanyuan Qu: yuanyuan.qu@campus.lmu.de
        - Jun-Wei To: J.To@campus.lmu.de
        - Aditya Bharadwaj: bharadwaj.aditya@campus.lmu.de
        Tutor: Francis Louis Constantin Arsene: francis04arsene1457@gmail.com

