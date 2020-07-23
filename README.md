# FishyBumpers
This was my submission for the Bumpers Homework Competition for the course Introduction to Software Engineering 2020 [IN0006](https://ase.in.tum.de/lehrstuhl_1/teaching/summer-2020/123-teaching/st19/1111-introduction-to-software-engineering-eist-summer-2020). The game took me about two days to implement, and about two more to make it pass the Continuous Integration Pipeline asserted by Artemis. I had to rename a lot of the classes I used, so the package names might not be straightforward. All assets used in the game are free-to-use (GNU license or equivalent) and most are made by me. The rest I have downloaded from the wonderful website opengameart.org, a website dedicated to sharing (as the name suggests) free-to-use game assets. I didn't get nominated or anything in the competition but I decided to make the project open-source with an MIT License, so that anyone can use the content in anyway they see fit.
## Introduction
FishyBumpers is a game about fish eating eachother. The idea of the homework competition was to implement a creative collision mechanism. My idea was to have a game, where the player controlls a fish and tries to eat fish smaller than himself/herself. When enough fish is eaten the player grows a level and can eat bigger fish. THe objective is to eat all the fish and win. There are 3 difficulty levels, with a hidden "Nightmare" difficulty only unlockable by defeating the game in "Hard" difficulty. The game also features a shark that attacks in random intervals entering from 6 random points on the screen.
## Controls
The player fish is controlled entirely with the mouse. Difficulty selection, pausing and starting the game can be done with the keyboard and/or mouse.
## Technical Details
The class that handles the game logic is [GameBoard.java](). The collision detection is done by [DevouringCollision.java](). The class [GameBoardUI.java]() handles the initialization and draws the textures.
## Screenshots
![Game Overview](https://i.imgur.com/1EFuIiu.png)
![Movement, Eating](https://i.imgur.com/GIEUAkI.gif)
![Shark Attack]()