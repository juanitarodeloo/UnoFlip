# UnoFlip
This repository will contain an Uno Flip game project for the course SYSC 3110: Software Development Project

#Milestone 1: Basic UNO Game
##Purpose of the Game:
The objective of this card game is for players to strategically play their cards with the aim of being the
first to exhaust all cards in their hand. Each player starts off with 0 points and when one player has no more cards
to play, they win that round and collect the points associated with the cards of the other opponents. The first player
to reach 500 points wins the entire game.

##Rules:

1) Objective: Be the first player to play all your cards and accumulate 500 points.

2) Gameplay: Players take turns to play a card.
   The card played must match the one currently on top of the deck either by number, color, or action.
   Special cards have unique actions that affect the flow of the game.
   If a player cannot play a card from their hand, they must draw one from the deck. Then they can choose to play the card
   they picked up or the game continues to the next player.
   If a player goes out by using any card that requires the next player to draw, that player must draw the
   appropriate number of cards before the scores are tallied.

3) Playing Cards: To play a specific card, enter the name of the card in lowercase with underscores replacing spaces.
   For example:
   For Wild Draw Two card, enter "wild_draw_two"
   For Draw One card, enter "draw_one"

4) Scoring:

When a player has played all their cards, they win that round and the hand concludes. This player scores points
based on the cards remaining in their opponents' hands:
* Number cards: Face value
* Draw One: 10 points
* Draw Five, Reverse, Skip, Flip: 20 points
* Skip Everyone: 30 points
* Wild: 40 points
* Wild Draw Two: 50 points
* Wild Draw Color: 60 points

5) Winning: The first player to accumulate 500 points across multiple rounds wins the game.

File Structure:

MainGame.py: Contains the primary game loop and gameplay logic.
MainGameTest.py: Contains all the test cases for the code.
Player.py: Defines the player class with attributes and methods specific to individual players.
Deck.py: Describes the deck class, handling card creation, shuffling, and distribution.
Card.py: Contains the card class definition, outlining the properties and behaviors of individual cards.

Data Structure Explanation:

Card Class:
* The Card class is made up of two enums, one to represent the Colour and one to represent the Type of the card.
  We chose to use enums since they are the easiest to extend and maintain. We know we will need to add colours and
  types in later milestones of this project, so using enums was essential for these attributes. For this milestone,
  there are a set amount of colours and types so we did not want to choose a data structure that allowed for dynamic
  setting and changing of these attributes. They are also type safe, which means that every time we compare them,
  they are guaranteed to not produce an incompatible type error.

Player Class:
* Each player has a hand of cards which are represented by an ArrayList. We thought this would be the best data
  structure since the number of cards a player has in their hand is constantly changing every round, so the ability
  to dynamically change this was essential. They start with a fixed length but as the game progresses they draw cards
  and put down cards. They needed the ability to play whatever card they wanted to so random access was another
  requirement for the data structure, and ArrayLists provide this in an efficient manner.

Deck Class:
* We chose to represent the deck object as an ArrayLists of cards because we wanted the ability to dynamically add and
  remove from this structure. We also wanted the ability to check the size and how many cards the deck contains in a
  moment of time. We also wanted to ability to search for elements using the indexOf() method in order to draw from the
  top of the deck. ArrayLists are the simplest data structure that met all of our requirements for the deck.

MainGame Class:
* The MainGame class is made up of two main data structures that contain the group of players and the group of cards
  that make up the discard pile. Both of these lists are ArrayLists because we needed to dynamically control the size
  of both lists. The players list depending on how many players the user entered, which we have no control over so this
  needed to be dynamically set. The player list also needed to be able to be randomly accessed since the first player
  is chosen at random. ArrayList provides an O(1) constant time to access elements by index, so this would be done
  efficiently. The discard list is constantly being added to during the duration of the game and emptied at the end,
  so the ability to change the size with the clear() method was a main reason why we chose to use an ArrayList for this
  list.

* Overall, we primarily chose ArrayLists for the list type data structure in our program because they are the most
  memory efficient and provide the most built-in functionality which would allow us to focus more on the logic of the
  game, and less on the individual data structures ability.

Authors:

Ayman Kamran
* Implemented the Deck, Player, Card class and their associated test classes
* Designed UML Class diagram

Adham Elmahi:
* Implemented MainGame functionalities: user prompts, card validation
* Designed sequence diagram
* Wrote README

Juanita Rodelo
* Implemented MainGame functionalities: user prompts, card validation, REVERSE action card, SKIP action card
* Wrote MainGame tests
* Wrote README

Rebecca Li
* Implemented MainGame functionalities: user prompts, card validation, DRAW ONE action card, WILD_DRAW_TWO action card,
  logic behind flow of the game
* Wrote MainGame tests