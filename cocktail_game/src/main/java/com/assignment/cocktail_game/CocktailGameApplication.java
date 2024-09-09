package com.assignment.cocktail_game;

import java.sql.*;
import java.util.*;

public class CocktailGameApplication {
	private static final String DB_URL = "jdbc:h2:~/test";
	private static final String USER = "sa";
	private static final String PASS = "";

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
			createTable(conn);
			playGame(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createTable(Connection conn) throws SQLException {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS HighScores (id INT AUTO_INCREMENT PRIMARY KEY, playerName VARCHAR(255), score INT)";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createTableSQL);
		}
	}

	private static void playGame(Connection conn) {
		try (Scanner scanner = new Scanner(System.in)) {
			;
			int score = 0;

			while (true) {
				Cocktail cocktail = getCocktail.getRandomCocktail();
				if (cocktail == null) {
					System.out.println("Failed to fetch cocktail. Exiting game.");
					break;
				}

				String cocktailName = cocktail.getName();
				String instructions = cocktail.getInstructions();
				String hiddenName = "_".repeat(cocktailName.length()).trim();
				String printName = hiddenName.replace("_", "_ ");
				int attempts = 5;

				System.out.println("Guess the cocktail: " + printName);

				System.out.println("Instructions: " + instructions);

				while (attempts > 0) {
					System.out.print("Your guess: ");
					String guess = scanner.nextLine();

					if (guess.equalsIgnoreCase(cocktailName)) {
						score += attempts;
						System.out.println("Correct! Your score: " + score);
						break;
					} else {

						attempts--;
						hiddenName = revealLetters(cocktailName, hiddenName);
						System.out.println("Wrong! Attempts left: " + attempts);
						printName = hiddenName.replace("_", "_ ");
						System.out.println("Hint: " + printName);

						// additional hints for the player depending on how many attempts left
						switch (attempts) {
						case 4:
							System.out.println("Drink is " + cocktail.getAlcoholic().toString());
							break;
						case 3:
							System.out.println("Glass used for the drink is " + cocktail.getGlass().toString());
							break;
						case 2:
							System.out.println("Category of the drink is " + cocktail.getCategory().toString());
							break;

						}

					}

					if (attempts == 0) {
						System.out.println("Game over! The cocktail was: " + cocktailName);
						saveHighScore(conn, score);
						printTop5HighScores(conn);
						return;
					}
				}
			}
		}
	}

	private static String revealLetters(String name, String hiddenName) {
		char[] hiddenArray = hiddenName.toCharArray();
		Random random = new Random();
		List<Integer> unrevealedIndices = new ArrayList<>();

		for (int i = 0; i < name.length(); i++) {
			if (hiddenArray[i] == '_') {
				unrevealedIndices.add(i);
			}
		}

		int lettersToReveal = name.length() > 5 ? 2 : 1;

		for (int i = 0; i < lettersToReveal && !unrevealedIndices.isEmpty(); i++) {
			int randomIndex = unrevealedIndices.remove(random.nextInt(unrevealedIndices.size()));
			hiddenArray[randomIndex] = name.charAt(randomIndex);
		}

		return new String(hiddenArray);
	}

	private static void saveHighScore(Connection conn, int score) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter your name: ");
			String playerName = scanner.nextLine();

			String insertSQL = "INSERT INTO HighScores (playerName, score) VALUES (?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
				pstmt.setString(1, playerName);
				pstmt.setInt(2, score);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void printTop5HighScores(Connection conn) {
		String query = "SELECT playerName, score FROM HighScores ORDER BY score DESC LIMIT 5";

		try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Top 5 High Scores:");
			while (rs.next()) {
				String playerName = rs.getString("playerName");
				int score = rs.getInt("score");
				System.out.println(playerName + ": " + score);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
