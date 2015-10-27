# Project Crawl

## Requirements
- [X] The app must enable players to start a new game and display a Sunka board in starting configuration.
- [X] Unless requirement 8 is implemented, the app must select a random player to make the first move at the start of the game.
- [X] When a player is permitted to make a turn, the app must enable that player to select a small tray on his/her side and redistribute the shells according to the rules of Sunka automatically.
- [X] The app must assign the next move to the correct player: i.e. the other player in the game, unless the player is entitled to an additional move or the other player is unable to make a move.
- [X] The app must implement the game rule pertaining to capturing an opponent's shells correctly.
- [X] The app must identify when the game is finished and identify the winner in that situation.
- [X] The source code should come with a full complement of automated tests of all the testable features that have been implemented.
- [X] The app should allow the players to execute their first turn simultaneously and recognise when each player has exhausted their moves.
- [X] The app should visualise the redistribution of shells in a way that players can observe.
- [ ] The app should include suitable graphics to visualise the board and shells to mimic the actual game.
- [ ] The app should collect statistics on games played, such as the number of games won/lost of each player, the time taken to make a move, high scores, player rankings, etc.
- [ ] The app may include some animations and sounds, provide these enhance usability and user experience.
- [ ] The app may allow two players to play a game from their own Android devices.
- [ ] The app may be extended with an AI player.
- [X] The app must not include a splash screen.

### Team Members
* Kyle
* Jonathan
* Charlie
* Adam
* Phileas

## Development
- Meet during lab times to work on features

### Conventions
 - Class names = ClassName extends OtherClass
 - Variables = String variableName = "";
 - Static final fields = FINAL_FIELD = 10;
 - JavaDoc public methods

## Pull Requests
- When a feature is completed, open a PR on bitbucket marking it with [Ready]
- Another team member will review it and make any comments, which the developer can then address
- If features need to be tweaked or added to
- Once happy, the PR can be merged into master.

### Pull Request Criterion
- Feature must function as expected
- Automated tests for both logic and UI must be present and must all pass

## Testing
- For views, test they they are present and they respond to user actions
- Test all methods that have logic
- Test methods that have a return value to ensure what is being returned is what is expected.
