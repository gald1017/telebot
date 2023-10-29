package flinkbot;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GetChatCompletionsSample {

    public static String QueryChatGPT(Iterable<flinkbot.InputMessage> messagesList) {
        //String azureOpenaiKey = System.getenv("AZURE_OPENAI_KEY");;
        String azureOpenaiKey = "7fab7d42514548598388d882af77e51f";
//        String endpoint = System.getenv("AZURE_OPENAI_ENDPOINT");;
        String endpoint = "https://connectorgpt.openai.azure.com/";
        String deploymentOrModelId = "TestChatGpt";

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(azureOpenaiKey))
                .buildClient();
//
//        List<ChatMessage> chatMessages = new ArrayList<>();
//        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, "You are a helpful assistant"));
//        chatMessages.add(new ChatMessage(ChatRole.USER, "Does Azure OpenAI support customer managed keys?"));
//        chatMessages.add(new ChatMessage(ChatRole.ASSISTANT, "Yes, customer managed keys are supported by Azure OpenAI?"));
//        chatMessages.add(new ChatMessage(ChatRole.USER, "Do other Azure AI services support this too?"));

        String listString = StreamSupport.stream(messagesList.spliterator(), false)
                .map(o -> o.text).collect(Collectors.joining(", "));

        String prompt = "those are telegram messages:\n" + listString + "please summarize them:\n";
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.USER, prompt));
        client.getChatCompletions(deploymentOrModelId, new ChatCompletionsOptions(chatMessages));
        ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelId, new ChatCompletionsOptions(chatMessages));
        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        List<String> result = new ArrayList<>();
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
//            System.out.println("Message:");
//            System.out.println(message.getContent());
            result.add(message.getContent());
        }

        return String.join(", ", result);
    }
}