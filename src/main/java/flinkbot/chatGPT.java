package flinkbot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class chatGPT {


    public static String QueryChatGPT(Iterable<flinkbot.InputMessage> messagesList) {
//        String url = "https://api.openai.com/v1/chat/completions";
        String url = "https://mda-tp-bot.openai.azure.com/";
        String apiKey = "51049b5a98dc4711b399d538a4118465";
        String model = "gpt-3.5-turbo";
//        String listString = String.join("\n ", messagesList);

        String listString = StreamSupport.stream(messagesList.spliterator(), false)
                .map(i -> i.toString()).collect(Collectors.joining(", "));

        String prompt = "those are telegram messages:\n" + listString + "please summarize them:\n";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }
}