package com.assignment.cocktail_game;

import java.sql.*;
import java.util.*;

public class CocktailGameApplication {
	private static final String DB_URL = "jdbc:h2:~/test";
	private static final String USER = "sa";
	private static final String PASS = "";

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
			Util.createTable(conn);
			playGame(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void playGame(Connection conn) {
		try (Scanner scanner = new Scanner(System.in)) {

			int score = 0;
			while (true) {
				Cocktail cocktail = Util.getRandomCocktail();
				if (cocktail == null) {
					System.out.println("Failed to fetch cocktail. Exiting game.");
					break;
				}

				String cocktailName = cocktail.getName();
				String instructions = cocktail.getInstructions();
				String hiddenName = "_".repeat(cocktailName.length()).trim(); // hiding the characters from the player
				String printName = hiddenName.replace("_", "_ "); // name to be shown to user
				int attempts = 5;

				System.out.println("Guess the cocktail: " + printName);

				System.out.println("Instructions: " + instructions);

				while (attempts > 0) {
					System.out.print("Your guess: ");
					String guess = scanner.nextLine(); // need to sanitise and limit input

					if (guess.equalsIgnoreCase(cocktailName)) {
						score += attempts;
						System.out.println("Correct! Your score: " + score);
						break;
					} else {

						attempts--;
						hiddenName = Util.revealLetters(cocktailName, hiddenName);
						System.out.println("Wrong! Attempts left: " + attempts);
						printName = hiddenName.replace("_", "_ ");
						System.out.println("Hint: " + printName);

						// additional hints for the player depending on how many attempts left
						switch (attempts) {
						case 4:
							System.out.println("Drink is " + cocktail.getAlcoholic());
							break;
						case 3:
							System.out.println("Glass used for the drink is " + cocktail.getGlass());
							break;
						case 2:
							System.out.println("Category of the drink is " + cocktail.getCategory());
							break;
						case 1:
							System.out.println("Category of the drink is " + cocktail.getCategory());
							break;
						case 0:
							System.out.println("Game over! The cocktail was: " + cocktailName);
							Util.saveHighScore(conn, score);
							Util.printTop5HighScores(conn);
							System.exit(0);

						default:
							System.out.println("Unexpected case, exiting game." + "attempts number is" + attempts);
							System.exit(0);

						}

					}

				}

			}
		}
	}

}
