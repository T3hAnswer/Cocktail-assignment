Assignment is to build a simple game of guess the cocktail using public API: https://www.thecocktaildb.com/api.php

  * Game rules:
    * Random cocktail with instructions (strInstructions) is shown to the player together with number of letter in the name of the cocktail (e.g. "Mojito" -> "______")
    * Player must guess the name of the cocktail
    * Player has 5 attempts to guess the name of the cocktail
    * If player answers correctly the game continues with a new random cocktail and score is increased by number of attempts left
    * If player answers wrongly or skips round the game shows:
      * Name of the cocktail with some new random letters revealed (e.g. "_ _ _ _ _ _" -> " _ _ j _ _ _" -> " _ _ j _ _ o" -> "M _ j _ _ o" -> "M _ ji _ o" -> "M _ jito" -> "Mojito") (For longer cocktails more letters can be revealed than one)
      * Reveal additional info about the cocktail (e.g. category, glass, ingredients, picture)
      * Number of attempts left
    * If player fails to guess the cocktail after 5 attempts the game ends and high score is updated
    * In one game same cocktail shouldn't appear twice

  * Resources:
    * Instructions on how to use the API are written here: https://www.thecocktaildb.com/api.php

  * Main tasks:
    * Build a Java console application using Java 1.8 or newer (we recommend using Java 17 or 21) which allows to play a game of "Guess the cocktail"
    * Build a game with the rules from above
    * Remember the current highest score achieved
    * Feel free to add any more functionalities you think would be cool to add

  * For additional credit:
    * Instead of console application, build a simple UI for the game, using your preferred framework (We prefer Angular, but feel free to use any Javascript framework you like). Game logic should still be in Java

  * Our expectations:
    * Your code is available on GitHub or any other VCS
    * Your code is clean and logically structured
    * Game logic is separated from other functionalities
    * Your code has tests where necessary
    * You are ready to explain your game & code
    * You are aware of the possible bugs in your game & code

NB! We have added a simple Spring Boot console application which you can use as the base for the project, but feel free to create your own Java project if you have any problems running this pre-setup.
