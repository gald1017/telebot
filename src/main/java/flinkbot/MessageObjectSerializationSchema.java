package flinkbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import javax.annotation.Nullable;

public class MessageObjectSerializationSchema implements KafkaRecordSerializationSchema<MessageObject> {

    static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final String topic;

    public MessageObjectSerializationSchema(String topic) {
        this.topic = topic;
    }

    @Override
    public void open(SerializationSchema.InitializationContext context, KafkaSinkContext sinkContext) throws Exception {
        KafkaRecordSerializationSchema.super.open(context, sinkContext);
    }

    @Nullable
    @Override
    public ProducerRecord<byte[], byte[]> serialize(MessageObject element, KafkaSinkContext context, Long timestamp) {
        try {
            return new ProducerRecord<>(
                    topic,
                    objectMapper.writeValueAsBytes(element)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
