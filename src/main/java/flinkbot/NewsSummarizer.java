package flinkbot;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.time.Instant;

public class NewsSummarizer<I, O, T extends Window> extends ProcessAllWindowFunction<InputMessage, NewsSummarization, TimeWindow> {
    @Override
    public void process(ProcessAllWindowFunction<InputMessage, NewsSummarization, TimeWindow>.Context context, Iterable<InputMessage> iterable, Collector<NewsSummarization> collector) throws Exception {
        InputMessage firstMsg = iterable.iterator().next();
        String summarization = GetChatCompletionsSample.QueryChatGPT(iterable);
        System.out.println("Done News Summarizer: " + summarization);
        collector.collect(
                new NewsSummarization(
                        summarization,
                        firstMsg.date,
                        firstMsg.chat_id
                ));
    }
}
