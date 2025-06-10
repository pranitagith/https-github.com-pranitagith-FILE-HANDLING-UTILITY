
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherFetcher {

    // Replace with your own API key from OpenWeatherMap
    private static final String API_KEY = "da6aa9c2616d6b1ebfc2987b56361e0e";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    public static void main(String[] args) {
        String city = "";
       try( Scanner scanner = new Scanner(System.in)){
        System.out.print("Enter city name: ");
        city = scanner.nextLine();
    }catch(Exception e) {
        e.printStackTrace();
    }

        try {
            String url = String.format(BASE_URL, city, API_KEY);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                displayWeather(response.body());
            } else {
                System.out.println("Failed to get weather data. Please check the city name.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }

    private static void displayWeather(String responseBody) {
        JSONObject obj = new JSONObject(responseBody);

        String cityName = obj.getString("name");
        double temp = obj.getJSONObject("main").getDouble("temp");
        int humidity = obj.getJSONObject("main").getInt("humidity");
        String weather = obj.getJSONArray("weather").getJSONObject(0).getString("description");

        System.out.println("\nWeather in " + cityName + ":");
        System.out.println("Temperature: " + temp + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Condition: " + capitalizeWords(weather));
    }

    private static String capitalizeWords(String text) {
        String[] words = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}