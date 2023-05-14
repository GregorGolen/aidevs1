import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObject;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Properties prop = new Properties();
        InputStream input = Main.class.getResourceAsStream("/config.properties");
        prop.load(input);
        String apiKey = prop.getProperty("api.key");

        HttpRequest authRequest = HttpRequest.newBuilder()
                .uri(new URI("https://zadania.aidevs.pl/token/helloapi"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json.createObjectBuilder()
                        .add("apikey", apiKey)
                        .build().toString()))
                .build();

        HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonResponse = Json.createReader(new java.io.StringReader(authResponse.body())).readObject();
        String token = jsonResponse.getString("token");

        HttpRequest taskRequest = HttpRequest.newBuilder()
                .uri(new URI("https://zadania.aidevs.pl/task/" + token))
                .GET()
                .build();

        HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        jsonResponse = Json.createReader(new java.io.StringReader(taskResponse.body())).readObject();
        String cookie = jsonResponse.getString("cookie");

        HttpRequest answerRequest = HttpRequest.newBuilder()
                .uri(new URI("https://zadania.aidevs.pl/answer/" + token))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json.createObjectBuilder()
                        .add("answer", cookie)
                        .build().toString()))
                .build();

        HttpResponse<String> answerResponse = client.send(answerRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Answer response code: " + answerResponse.statusCode());
        System.out.println("Answer response headers: " + answerResponse.headers());
        System.out.println("Answer response body: " + answerResponse.body());

    }
}

