package spendreport;

import com.esotericsoftware.kryo.NotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

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
//        return JsonUtils.getEventJsonMapper().readValue(message, EventHubMessageEnvelope.class);
//        long currentTime = DateTime.now(DateTimeZone.UTC).getMillis();
        InputMessage inputMessage = objectMapper.readValue(consumerRecord.value(), InputMessage.class);
        long millisecondsAsLong = (long) inputMessage.date; // Convert float to long
        Instant instant = Instant.ofEpochMilli(millisecondsAsLong);

        System.out.println("Instant: " + instant);
//        collector.collect(getSourceRecord(consumerRecord, inputMessage, currentTime));
        collector.collect(inputMessage);
    }

//    @NotNull
//    private InputMessage getSourceRecord(ConsumerRecord<byte[], byte[]> record, String message, long currentTime) {
//        return new InputMessage(
//                message,
//                String.format("%s.%s", record.topic(), record.partition()),
//                partitionInitializationTime,
//                record.offset(),
//                record.timestamp(),
//                currentTime,
//                null // TODO: check if ever needed, initial check indicate this was only relevant to `addAllFieldsToEvents` by idoBarav, which pretty much broke SEEP, so not needed
//        );
//    }

}