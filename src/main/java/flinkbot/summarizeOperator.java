package flinkbot;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.time.Instant;

public class summarizeOperator<I, O, T extends Window> extends ProcessAllWindowFunction<InputMessage, Object, TimeWindow> {
    @Override
    public void process(ProcessAllWindowFunction<InputMessage, Object, TimeWindow>.Context context, Iterable<InputMessage> iterable, Collector<Object> collector) throws Exception {
        System.out.println("summarizeOperator");

        for (InputMessage msg : iterable) {
            System.out.println("message: " + msg.text);
            System.out.println("time: " + Instant.ofEpochMilli(msg.date));
            collector.collect(msg.text);
        }
        chatGPT.QueryChatGPT(iterable);
        System.out.println("done summarizeOperator");
    }
}
