# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html)

# MODEL & VIEW :
###  Assignees : _Ugur Dogan, Jun-Wei To and Aditya Bharadwaj_
# NETWORK:
### Assignees : _Yili Li and Yuanyuan Qu_

(The responsible teammates' names written after every entry in the brackets)

## 2024-07-14
### Added
- Grayed out text selection for non-hosts(Jun-Wei To)
- complete CheckStyle and SpotLess(Aditya Bharadwaj)
- Completed JavaDoc(Aditya Bharadwaj and Yuanyuan Qu)
- Readme with new features added.(Yili Li)
- Test classes of Client and Server added. (Yuanyuan and Yili)

## Fixed
- UFO going off the progress bar in results(Jun-Wei)
- Dad jokes category being unselectable(Jun-Wei)
- Special characters in texts (Jun-Wei)

## 2024-07-13

### Added
- Handler for too long names(Jun-Wei)
- Exit button in lobby sends you back to main menu(Jun-Wei)
- Main menu button in results(Jun-Wei)
- Grayed out start game button for non-hosts(Jun-Wei)
- Readme merged in main.(Yili)
- Progress Bar on Game/Result Screen (Aditya Bharadwaj)

### Fixed
- Host can't have an empty name(Jun-Wei)
- Game lock when 6 players are in the lobby(Jun-Wei)
- Can't start a new round when current one isn't finished yet(Jun-wei)

## 2024-07-12

### Added
- Settings Windows (Aditya Bharadwaj)
- New Text Categories (Aditya Bharadwaj)
- Sounds Toggle- On/Off(Aditya Bharadwaj)
- New result screen background and visuals(Aditya Bharadwaj)
- Counter for the number of wrong inputs(Jun-Wei)

### Fixed
- Player characters being misaligned on the end results (Jun-wei)
- Accuracy going above 100 (only happens with too many inputs at once) - (Jun-Wei)

## 2024-07-11

## Added
- runGui and runServer commands for terminal (Yili)
- gradle dependencies (Aditya and Yili)
- new way of compiling instruction added in README (Yili)

### Fixed
- lobby full: show label "lobby full start the game", when one player left, label disappear (Yuanyuan)

## 2024-07-10
### Fixed
- join game by entering ip-adress (Yili)

## 2024-07-09
### Added
- New test classes: MockInputStream, MockServerSocket, ServerTest, Sleeps, TestUtils (Yuanyuan- Yili)

### Fixed
- Only the first player of lobby can start game (host) (Yuanyuan)
- all the backgrounds were changed (login, client and gamescreen) (Aditya)


## 2024-07-08
### Added
- GameScreen: synchronization of WPM after each car (Yuanyuan)
- Only host (first joined player) can start the game (messagetype) - (Yuanyuan)
- Readme (Yili)
- Sounds in the Game (Aditya B)

- new network package for test (Yili and Yuanyuan)
  - MockSocket (Yili)
- handleClientDisconnection method (Yili)
### Fixed
- Errors by exiting the game fixed. (Yili)
-  Reformatted the CHANGELOG from the start (Aditya B)

## 2024-07-07
### Fixed

- GameState Update()
- ResultScreen Visuals (Jun-Wei)

## 2024-07-06
### Added

- JUnit Tests for GameState (Aditya B)
- JavaDoc in CI Pipeline (Aditya B)

## 2024-07-05
### Added

- New Background Options (Aditya B)
- New Font for Game Screen and Results Screen (Aditya B)

### Fixed

- Repository Structure to MVC (Yuanyuan and Ugur)
- Game Timer
- Exit Option (when player clicks cross, they exit the game) (Jun-Wei)

## 2024-07-04
### Added
- new MessageTypes: UpdateRankingRequest, RankingNotification (Yuanyuan)
- hostname -> online multiplayer mode on different computers (Yili and Yuanyuan)
- Timer starts when the round begins (instead of when the player starts typing) (Jun-Wei)

### Fixed
- ranking list update and synchronization (Yuanyuan)

### Changed
- Newest Sequence Diagramm according to our current version (Yili)
- Newest Json Protocal according to our current version (Yuanyuan)

## 2024-07-03
### Added

- Ranking List on Results Screen (Jun-Wei)
- Ranking Broadcast (Yili and Yuanyuan)
-

### Fixed

- Game Screen View, Background (Aditya B)
- Text Class (Aditya B)

## 2024-07-02
### Added
- ClientController, ResultScreen: Update the personal result screen according to the result in GameEndedNotification. (Yuanyuan)
- Gamescreen Dark Background (Aditya B)
- Typed words Color Implementation (Green, if correct; Red, if incorrect) (Aditya B)

### Fixed
- Server, GameScreen: Every client game screen will show the same text at the same round. (Yuanyuan)
- ClientView, ConnectionManager: Fixed that no player list shows in lobby after the click of start new game and player cannot be deleted when they exit. (Yili)
- Sync Text on different players game screens (Yuanyuan)
- Restart Button (Transitions back to ClientWindow) (Yuanyuan)
- WPM on the Results Window (Jun-Wei)

## 2024-06-29
### Fixed
- Server: Player will be deleted from player list when they exit, also in playing window and ranking window. (Yili)
- GameScreen: Fixed car movement and the screen synchronization (Yuanyuan)
- Handler for closing the window while in waiting room (Yuanyuan and Aditya)
- PlayerLeft Notification in ClientWindow (Aditya and Yuanyuan)

## 2024-06-28
### Added
- ClientController, GameScreen: Update the player's game status on the GameScreen with
  the player's progress in the GameStateNotification sent by the server. (Yuanyuan)
- New Text samples in _Text Class_ (Aditya B)
- Car Shape Class (Ugur D)

### Fixed

- Game Logic Updated (Aditya B and Ugur)
- Typing Player Class (Ugur)


## 2024-06-25
### Added
- Model View and Network Integration (Aditya B)
- ClientWindow (Aditya B)
- LobbyFullNotification (Aditya B)
- PlayerListUpdateNotification (Aditya B)
- Result Window (Aditya, Jun-Wei and Ugur)

### Changed
- Changed the Message class into  MessageType class, no more need to extends from this class (Yuanyuan)
- Methods in _ClientController_: HandlePlayerListUpdate(), HandleLobbyFull() (Yuanyuan)
- Methods in _GameServer_: BroadcastPlayerListUpdate(), BroadcastMessages(), BroadcastLobbyFull() (Yili)

### Removed
- all extends are removed (Yuanyuan)

### Fixed
- GameServer (Yili)
- GameClient (Yuanyuan)
- ClientController (Yuanyuan)
- GameScreen Update (Ugur)

## 2024-06-24
### Added
- ClientController (Yuanyuan)
- GameClient (Yuanyuan)
- GameServer (Yili)
- ConnectionManager (Yili)
- synchronized the processMessage class (Yuanyuan and Yili)
- Initial Structure of Car Class, Text Class and Player Interface (Ugur and Aditya)
- new class: message (Yili)
- new class : PlayerListUpdateNotification in Messages package (Yili)

### Changed
- Changed the Interface of server and client into two separated interfaces (Yuanyuan and Yili)
- Changed the Message interface to a normal class (Yuanyuan)


## 2024-06-23
### Added
- Race Class (Ugur)
- GameState Class (Ugur)
- GameScreen Class in VIEW (Aditya B)
- TypingPlayer (Ugur and Aditya)

### Removed

- Typer Class (Confusing to understand) (Aditya B)
- LoginWindow Class(instead moved them to view package) (Aditya B)
- ResultWindow Class(instead moved them to view package) (Aditya B)
- GameWindow Class(instead moved them to view package) (Aditya B)


## 2024-06-22

### Added
- new class: messages, request for server and notifications for client (Yuanyuan and Yili)

### Fixed

- Game Interface (Ugur)
- Player Interface (Ugur)
- GameState (Ugur)
- WPM & Accuracy (Ugur and Aditya)


## 2024-06-16
### Added

- Initial packages for the development (Based on MVC Pattern) (Aditya B)

## 2024-06-15
### Added
- Game Mockups into the Repo (Ugur and Aditya )

## 2024-06-14

### Added

- Sequence Diagram (Yili and Yuanyuan)
- Team Policy (Aditya)
