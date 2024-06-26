# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Race code
- GameScreen Class in View Package
- Typing Player (instead of Typer)

### Changed
GUI Class(with Main Method)
### Removed
- LoginWindow Class
- ResultWindow Class
- GameWindow Class
- Typer Class
- 
### Fixed

- Game Interface
- Player Interface



# NETWORK
## 22.Jun.2024
### Added
- new class: messages, request for server and notis for client

## 24.Jun.2024
### Added
- ClientController
- GameClient
- GameServer
- ConnectionManager
- synchronized the processMessage class

### Changed
- Changed the Interface of server and client into two separated interfaces
- Changed the Message interface to a normal class


## 25.Jun.2024
### Changed
- Changed the Message class into  MessageType class, no more need to extends from this class

### Removed
- all extends are removed 