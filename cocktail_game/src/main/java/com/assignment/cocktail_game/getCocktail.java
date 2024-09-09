package com.assignment.cocktail_game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class getCocktail {
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
            
            
            if (avoidDuplicateCocktails.contains(cocktail.getName())) {
                cocktail = getCocktail.getRandomCocktail();
            }
            
            avoidDuplicateCocktails.add(cocktail.getName());
            

            return cocktail;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
