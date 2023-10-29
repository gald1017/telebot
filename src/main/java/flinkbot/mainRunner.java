/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flinkbot;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.connector.kafka.source.KafkaSourceBuilder;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Properties;

/**
 * Skeleton code for the datastream walkthrough
 */
public class mainRunner {

    private static final String TOPIC = "telegram-bot";
    private static final String FILE_PATH = "src/main/resources/consumer.config";

    public static final String SOURCE_UID_PREFIX = "KafkaSource";

    public static DataStream<InputMessage> getSourceStreamFromConfig(StreamExecutionEnvironment env) throws IOException {


        Properties sourceConfig = new Properties();
        sourceConfig.load(new FileReader(FILE_PATH));


        KafkaSourceBuilder<InputMessage> builder = KafkaSource.<InputMessage>builder()
                .setBootstrapServers(sourceConfig.getProperty("bootstrap.servers"))  // bootstrapServers = "Cas-RS02-EU-Pipeline1-Primary.servicebus.windows.net:9093";
                .setTopics(TOPIC)
                .setGroupId(sourceConfig.getProperty("group.id"))
                .setDeserializer(new InputMessageDeserializationSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .setProperties(sourceConfig);

        return env.fromSource(
                builder.build(),
                WatermarkStrategy.noWatermarks(), //.withIdleness(Duration.ofSeconds(10)), // TODO: change to event time
                SOURCE_UID_PREFIX + sourceConfig.getProperty("bootstrap.servers")
        ).uid(SOURCE_UID_PREFIX + sourceConfig.getProperty("bootstrap.servers"));
    }


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<InputMessage> stream = getSourceStreamFromConfig(env)
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<InputMessage>(Time.seconds(50)) {
                    @Override
                    public long extractTimestamp(InputMessage inputMessage) {
                        return inputMessage.date;
                    }
                });
        // pipeline 1
        // 1. key by chat id
        // 2. enrich message count
        // 3. trigger alert?

        //		stream.keyBy(InputMessage::getChat_id).process(msCountEnricher()).trigger(new CountTrigger(5)).print();


        // pipeline 2 - for Hebrew:
        // 1. windowing
        // 3. summarize
        // write to sink

        stream
                .filter(new isHebrew())
                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(60))).allowedLateness(Time.seconds(5)) // TODO: change to event time
                .process(new summarizeOperator<InputMessage, Object, TimeWindow>()); // TODO: should by async?
//        .addSink();


        // pipeline 2 - for Arabic:
        // 1. windowing
        // 2. translate
        // 3. summarize
//        // write to sink


//        stream.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(60))) // TODO: change to event time
//                .allowedLateness(Time.seconds(10)).process(new ProcessAllWindowFunction<InputMessage, Object, TimeWindow>() {
//                    @Override
//                    public void process(ProcessAllWindowFunction<InputMessage, Object, TimeWindow>.Context context, Iterable<InputMessage> iterable, Collector<Object> collector) throws Exception {
//                        System.out.println("im processing 10");
//                        for (InputMessage msg : iterable) {
//                            System.out.println("message: " + msg.text);
//                            System.out.println("time: " + Instant.ofEpochMilli(msg.date));
//                            collector.collect(msg.text);
//                        }
//                    }
//                }); // pipeline 2
//

//		DataStream<Alert> alerts = transactions
//			.keyBy(Transaction::getAccountId)
//			.process(new FraudDetector())
//			.name("fraud-detector");
//
//		alerts
//			.addSink(new AlertSink())
//			.name("send-alerts");

        env.execute("Tele-Bot");
    }
}
