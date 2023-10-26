package spendreport;

import com.esotericsoftware.kryo.NotNull;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.time.Instant;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.*;

public class InputMessageDeserializationSchema implements
        KafkaRecordDeserializationSchema<InputMessage> {

    static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

        InputMessage inputMessage = objectMapper.readValue(consumerRecord.value(), InputMessage.class);

        Instant instant = Instant.ofEpochMilli(inputMessage.date);

        System.out.println("Instant: " + instant);
        collector.collect(inputMessage);
    }
}