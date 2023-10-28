package flinkbot;

public class isHebrew implements org.apache.flink.api.common.functions.FilterFunction<InputMessage> {
    @Override
    public boolean filter(InputMessage inputMessage) throws Exception {
        return inputMessage.is_hebrew;
    }
}
