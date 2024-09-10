package com.assignment.cocktail_game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;


public final class Util {
	
	  private Util() {
		    throw new IllegalStateException("Utility class");
		  }
	//fetches a random cocktail from cocktailDB
	public static Cocktail getRandomCocktail() {
        try {
            List<String> avoidDuplicateCocktails = new ArrayList<>();

        	
            // Create an HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create a request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://www.thecocktaildb.com/api/json/v1/1/random.php"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.body()) ;
            JSONArray drinksArray = jsonResponse.getJSONArray("drinks");
            JSONObject drink = drinksArray.getJSONObject(0);

            // Create a Cocktail object
            Cocktail cocktail = new Cocktail();
            cocktail.setName(drink.getString("strDrink"));
            cocktail.setCategory(drink.getString("strCategory"));
            cocktail.setAlcoholic(drink.getString("strAlcoholic"));
            cocktail.setGlass(drink.getString("strGlass"));
            cocktail.setInstructions(drink.getString("strInstructions"));
            
            //logic to avoid having the same cocktail show up more than once
            if (avoidDuplicateCocktails.contains(cocktail.getName())) {
                cocktail = Util.getRandomCocktail();
            }    
            avoidDuplicateCocktails.add(cocktail.getName());
            client.close();
            return cocktail;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	//saves high scores to DB
	static void saveHighScore(Connection conn, int score) {
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

	//Checks if highscore table exist and if it doesn't it creates one
	static void createTable(Connection conn) throws SQLException {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS HighScores (id INT AUTO_INCREMENT PRIMARY KEY, playerName VARCHAR(255), score INT)";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createTableSQL);
		}
	}

	//prints top 5 scores to the player
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

	//handles revealing of the letters based on number of attempts
	static String revealLetters(String name, String hiddenName) {
		char[] hiddenArray = hiddenName.toCharArray();
		Random random = new Random();
		List<Integer> unrevealedIndices = new ArrayList<>();
	
		for (int i = 0; i < name.length(); i++) {
			if (hiddenArray[i] == '_') {
				unrevealedIndices.add(i);
			}
		}
		
		int lettersToReveal = name.length() > 5 ? 2 : 1; //reveal more letters if cocktail.name is over 5 characters long
	
		for (int i = 0; i < lettersToReveal && !unrevealedIndices.isEmpty(); i++) {
			int randomIndex = unrevealedIndices.remove(random.nextInt(unrevealedIndices.size()));
			hiddenArray[randomIndex] = name.charAt(randomIndex);
		}
	
		return new String(hiddenArray);
	}

}
