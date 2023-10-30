package flinkbot;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NewsSummarizer<I, O, T extends Window> extends ProcessAllWindowFunction<InputMessage, NewsSummarization, TimeWindow> {
    @Override
    public void process(ProcessAllWindowFunction<InputMessage, NewsSummarization, TimeWindow>.Context context, Iterable<InputMessage> iterable, Collector<NewsSummarization> collector) throws Exception {
        int message_length = ((Collection<?>) iterable).size();
        String listString = StreamSupport.stream(iterable.spliterator(), false)
                .map(o -> o.text).collect(Collectors.joining(". \n "));

        if (message_length < 1) {
            return;
        }
        InputMessage firstMsg = iterable.iterator().next();
        try {
            String summarization = GetChatCompletionsSample.QueryChatGPT(iterable);
            System.out.println("Summarized : " + message_length + "\n messages: " + listString + "\n");
            System.out.println("Done News Summarizer: " + summarization);
            collector.collect(
                    new NewsSummarization(
                            summarization,
                            firstMsg.date,
                            firstMsg.chat_id
                    ));
        }
    catch (Exception e) {
        System.out.println("Error in News Summarizer: " + e.getMessage());
        e.printStackTrace();
    }
    }
    }

