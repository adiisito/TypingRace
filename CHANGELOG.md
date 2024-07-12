# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]


# MODEL & VIEW : 
###  Assignees : _Ugur Dogan, Jun-Wei To and Aditya Bharadwaj_
# NETWORK:  
### Assignees : _Yili Li and Yuanyuan Qu_

## 2024-07-12

## Added
- New result screen background and visuals

## Fixed
- Player character being misaligned on the end results

## 2024-07-11

## Added
- runGui and runServer commands for terminal
- gradle dependencies
- new way of compiling instruction added in README

### Fixed
- lobby full: show label "lobby full start the game", when one player left, label disappear

## 2024-07-10
### Fixed
- join game by entering ip-address

## 2024-07-09
### Added
- New test classes: MockInputStream, MockServerSocket, ServerTest, Sleeps, TestUtils

### Fixed
- Only the first player of lobby can start game (host)
- all the backgrounds were changed (login, client and gamescreen)


## 2024-07-08
### Added
- GameScreen: synchronization of WPM after each car
- Only host (first joined player) can start the game (messagetype)
- Readme
- Sounds in the Game

- new network package for test
  - MockSocket
- handleClientDisconnection method
### Fixed
- Errors by exiting the game fixed.
-  Reformatted the CHANGELOG from the start 

## 2024-07-07
### Fixed 

- GameState
- ResultScreen Visuals

## 2024-07-06 
### Added

- JUnit Tests for GameState 
- JavaDoc in CI Pipeline

## 2024-07-05
### Added

- New Background Options 
- New Font for Game Screen and Results Screen

### Fixed

- Repository Structure to MVC 
- Game Timer
- Exit Option (when player clicks cross, they exit the game)

## 2024-07-04
### Added
- new MessageTypes: UpdateRankingRequest, RankingNotification
- hostname -> online multiplayer mode on different computers
- Timer starts when the round begins (instead of when the player starts typing)

### Fixed
- ranking list update and synchronization

### Changed
- Newest Sequence Diagramm according to our current version
- Newest Json Protocal according to our current version

## 2024-07-03
### Added 

- Ranking List on Results Screen 
- Ranking Broadcast
- 

### Fixed 

- Game Screen View, Background
- Text Class

## 2024-07-02
### Added
- ClientController, ResultScreen: Update the personal result screen according to the result in GameEndedNotification.
- Gamescreen Dark Background
- Typed words Color Implementation (Green, if correct; Red, if incorrect)

### Fixed
- Server, GameScreen: Every client game screen will show the same text at the same round.
- ClientView, ConnectionManager: Fixed that no player list shows in lobby after the click of start new game and player cannot be deleted when they exit.
- Sync Text on different players game screens
- Restart Button (Transitions back to ClientWindow)
- WPM on the Results Window

## 2024-06-29
### Fixed
- Server: Player will be deleted from player list when they exit, also in playing window and ranking window.
- GameScreen: Fixed car movement and the screen synchronization
- Handler for closing the window while in waiting room
- PlayerLeft Notification in ClientWindow

## 2024-06-28
### Added
- ClientController, GameScreen: Update the player's game status on the GameScreen with the player's progress in the GameStateNotification sent by the server.
- New Text samples in _Text Class_
- Car Shape Class
- 
### Fixed 

- Game Logic 
- Typing Player Class


## 2024-06-25
### Added 
- Model View and Network Integration
- ClientWindow
- LobbyFullNotification
- PlayerListUpdateNotification
- Result Window

### Changed
- Changed the Message class into  MessageType class, no more need to extends from this class
- Methods in _ClientController_: HandlePlayerListUpdate(), HandleLobbyFull()
- Methods in _GameServer_: BroadcastPlayerListUpdate(), BroadcastMessages(), BroadcastLobbyFull()

### Removed
- all extends are removed

### Fixed
- GameServer
- GameClient
- ClientController
- GameScreen

## 2024-06-24
### Added
- ClientController
- GameClient
- GameServer
- ConnectionManager
- synchronized the processMessage class
- - Initial Structure of Car Class, Text Class and Player Interface
- new class: message,
- new class : PlayerListUpdateNotification in Messages package

### Changed
- Changed the Interface of server and client into two separated interfaces
- Changed the Message interface to a normal class


## 2024-06-23
### Added
- Race Class
- GameState Class
- GameScreen Class in VIEW
- TypingPlayer

### Removed 

- Typer Class (Confusing to understand)
- LoginWindow Class(instead moved them to view package)
- ResultWindow Class(instead moved them to view package)
- GameWindow Class(instead moved them to view package)


## 2024-06-22

### Added
- new class: messages, request for server and notifications for client

### Fixed 

- Game Interface
- Player Interface
- GameState
- Typed Text Colors according to if its correct or incorrect & BG Color
- WPM & Accuracy


## 2024-06-16
### Added 

- Initial packages for the development (Based on MVC Pattern)

## 2024-06-15
### Added
- Game Mockups into the Repo

## 2024-06-14 
 
### Added 

- Sequence Diagram
- Team Policy
