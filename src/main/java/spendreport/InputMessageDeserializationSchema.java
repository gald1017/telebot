package spendreport;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class InputMessageDeserializationSchema implements
        KafkaRecordDeserializationSchema<InputMessage> {


    static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Override
    public TypeInformation<InputMessage> getProducedType() {
        return TypeInformation.of(InputMessage.class);
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        KafkaRecordDeserializationSchema.super.open(context);
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<InputMessage> collector) throws IOException {

    }
}