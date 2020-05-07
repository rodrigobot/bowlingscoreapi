# Bowling Score API
A RestFul API that can be use to manage the score of a bowling game

# Requirements
Given a valid sequence of rolls for one line of American Ten-Pin Bowling, produces the total score for the game. This is a summary of the rules of the game:

* Each game, or "line" of bowling, includes ten turns, or "frames"for the bowler.
* In each frame, the bowler gets up to two tries to knock down all the pins.
* If in two tries, he fails to knock them all down, his score for that frame is the total number of pins Knocked down in his two tries.
* If in two tries he knocks them all down, this is called a "spare" and his score for the frame is ten plus the number of pins knocked down on his next throw (in his next turn).
* If on his first try in the frame he knocks down all the pins, this is called a "strike" His turn is over, and his score for the frame is ten plus the simple total of the pins knocked down in his next two rolls.
* If he gets a spare or strike in the last (tenth) frame, the bowler gets to throw one or two more bonus balls, respectively. - These bonus throws are taken as part of the same turn. If the bonus throws knock down all the pins, the process does not repeat: the bonus throws are only used to calculate the score of the final frame.

# How to use
Run Integration Tests, Unit Test and create an executable jar

```mvn clean package spring-boot:repackage```

Start API server

```java -jar target/bowlingscore-0.0.1-SNAPSHOT.jar```

# API Endpoints
### Index
The Bowling Score API provides an endpoint at the root route which returns a String message.

```GET /```

## Example

```curl localhost:8080/```

### Start Game
The Bowling Score API provides an endpoint to tell the system that a new games needs to be initialized.

```POST /game```

## Example

```curl -X POST localhost:8080/game```

### Get Game Information
The Bowling Score API provides an endpoint to retrieve game information. The endpoint returns a JSON object with the following information:
1. Game ID
2. Players list
3. Frames played per player

```GET /game/{gameId}```

## Example

```curl localhost:8080/game/{gameId}```

### Add a Player to Game
The Bowling Score API provides an endpoint to add a list of players to a game. The endpoint returns a JSON object with the following information:
1. Game ID
2. Players list
3. Frames played per player
The endpoint accepts a JSON list with the name of each player.

```POST /game/{gameId}/player```

## Example

```curl -H "Content-type: application/json" -X POST --data '["test1", "test2"]' localhost:8080/game/{gameId}/player```

### Roll Result
The Bowling Score API provides and endpoint to submit a the number of pins knocked down. It returns a JSON object representing a Player information.

```PUT /game/{gameId}/player/{playerName}/roll/{pins}```

## Example

```curl -X PUT localhost:8080/game/1/test/roll/4```

### Get Score of a Player's Game
The Bowling Score API provides an endpoint to retrieve the score of a player's game.
```GET /game/{gameId}/player/{playerName}/score```

## Example

```curl localhost:8080/game/1/player/test/score```
