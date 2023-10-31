package flinkbot;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class gptAsync extends RichAsyncFunction<MessageObject, MessageObject> {
    String azureOpenaiKey = "7fab7d42514548598388d882af77e51f";
    String endpoint = "https://connectorgpt.openai.azure.com/";
    private transient OpenAIClient client;

    @Override
    public void open(Configuration parameters) throws Exception {

        client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(azureOpenaiKey))
                .buildClient();
    }

    @Override
    public void asyncInvoke(MessageObject concatenatedMessages, ResultFuture<MessageObject> resultFuture) throws Exception {
        System.out.println("trying to summarize: " + concatenatedMessages.message_count + " messages. The messages are:"
                + concatenatedMessages.text + "\n" + "is hebrew: " + concatenatedMessages.is_hebrew + "\n");

        CompletableFuture.supplyAsync(() -> {
            try {
                return GetChatCompletionsSample.QueryChatGPT(concatenatedMessages.text, client);
            } catch (Exception e) {
                System.out.println("Error in News Summarizer: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }).thenAccept((String summarization) -> {
            System.out.println("Done News Summarizer: \n" + summarization);

            resultFuture.complete(
                    Collections.singleton(
                            new MessageObject(
                                    summarization,
                                    concatenatedMessages.date,
                                    concatenatedMessages.chat_id,
                                    concatenatedMessages.is_hebrew
                            )
                    )
            );
        });

//
//        try {
//            String summarization = GetChatCompletionsSample.QueryChatGPT(concatenatedMessages.concatenated_messages, client);
//            System.out.println("Done News Summarizer: \n" + summarization);
//
//            resultFuture.complete(
//                    Collections.singleton(
//                            new NewsSummarization(
//                                    summarization,
//                                    concatenatedMessages.date,
//                                    concatenatedMessages.chat_id
//                            )
//                    )
//            );
//        }
//        catch (Exception e) {
//            System.out.println("Error in News Summarizer: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    }
}
