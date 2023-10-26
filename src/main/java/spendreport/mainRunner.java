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

package spendreport;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.connector.kafka.source.KafkaSourceBuilder;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.connector.kafka.source.KafkaSource;

import java.io.FileReader;
import java.io.IOException;
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
				WatermarkStrategy.noWatermarks(),
				SOURCE_UID_PREFIX + sourceConfig.getProperty("bootstrap.servers")
		).uid(SOURCE_UID_PREFIX + sourceConfig.getProperty("bootstrap.servers"));
	}



	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
//		DataStreamSource<InputMessage> stream = env.addSource(new FlinkKafkaConsumer011<>(TOPIC, new InputMessageDeserializationSchema(), properties));
		DataStream<InputMessage> stream = getSourceStreamFromConfig(env);
		stream.print();



//		DataStream<Transaction> transactions = env
//			.addSource(new TransactionSource())
//			.name("transactions");
//
//		DataStream<Alert> alerts = transactions
//			.keyBy(Transaction::getAccountId)
//			.process(new FraudDetector())
//			.name("fraud-detector");
//
//		alerts
//			.addSink(new AlertSink())
//			.name("send-alerts");

		env.execute("Fraud Detection");
	}
}
