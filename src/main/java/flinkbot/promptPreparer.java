package flinkbot;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class promptPreparer extends ProcessAllWindowFunction<InputMessage, ConcatenatedMessages, TimeWindow> {

    @Override
    public void process(ProcessAllWindowFunction<InputMessage, ConcatenatedMessages, TimeWindow>.Context context, Iterable<InputMessage> iterable, Collector<ConcatenatedMessages> collector) throws Exception {
        String listString = StreamSupport.stream(iterable.spliterator(), false)
                .map(o -> o.text).collect(Collectors.joining(". \n "));
        int message_length = ((Collection<?>) iterable).size();

        if (message_length < 1) {
            return;
        }

        InputMessage firstMsg = iterable.iterator().next();

        collector.collect(
                new ConcatenatedMessages(
                        listString,
                        firstMsg.date,
                        firstMsg.chat_id,
                        message_length
                ));
    }
}
