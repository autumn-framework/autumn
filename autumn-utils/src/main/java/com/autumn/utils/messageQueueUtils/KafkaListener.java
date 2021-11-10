package com.autumn.utils.messageQueueUtils;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.autumn.utils.exceptions.MessageNotFoundInKafkaConsumerException;
import com.autumn.reporting.extentReport.Logger;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.avro.Schema;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;


public class KafkaListener {

    private static volatile KafkaListener instance;

    private KafkaListener() {
    }

    public static KafkaListener getInstance() {
        if (instance == null) {
            synchronized (KafkaListener.class) {
                if (instance == null) {
                    instance = new KafkaListener();
                }
            }
        }
        return instance;
    }

    public Consumer<String, byte[]> createConsumer(String KafkaConsumer,String KafkaTopic,String KafkaBootstapServer) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBootstapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.setProperty("specific.avro.reader", "true");

        // Create the consumer using props.
        final Consumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(KafkaTopic));
        Logger.logInfoInLogger("Consumer connection done");
        return consumer;
    }

    public synchronized ArrayList<String> runConsumer(String KafkaConsumer,String KafkaTopic,String KafkaBootstapServer,Schema schema) throws IOException {
        captureKafkaDetails(KafkaTopic,KafkaConsumer,KafkaBootstapServer);
        Logger.logInfoInLogger("Running Consumer --- " +KafkaConsumer+" on Topic "+KafkaTopic);
        ArrayList<String> consumerRecord= new ArrayList<String >();
        final Consumer<String, byte[]> consumer = createConsumer(KafkaConsumer,KafkaTopic,KafkaBootstapServer);
        final int giveUp = 100;   int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<String, byte[]> consumerRecords =
                    consumer.poll(20);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            for (ConsumerRecord<String, byte[]> record : consumerRecords) {
                record.value();
                try {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(record.value());
                    BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(record.value(), byteBuffer.position(), byteBuffer.remaining(), null);
                    GenericRecord data = new GenericDatumReader<GenericRecord>(schema).read(null, decoder);
                    consumerRecord.add(data.toString());
                }catch (Exception e){
                    consumerRecord.add(new String(record.value()));
                }
            }

            consumer.commitAsync();
        }
        consumer.close();
        capturekafkaConsumerMessages(consumerRecord);
        Logger.logInfoInLogger("DONE");
        return consumerRecord;
    }

    public void captureKafkaDetails(String KafkaTopic,String KafkaConsumer,String KafkaBootstapServer){
        Logger.logInfo("Kaftka details are :<br> Bootstrap Server : "+ KafkaBootstapServer+"<br>Kaftka Topic : "+KafkaTopic+"<br>KaftkaConsumer : "+KafkaConsumer);
    }

    public void capturekafkaConsumerMessages(ArrayList<String> consumerRecord){
        if(consumerRecord.size()!=0){
            String message="";
            for (int i=0;i<consumerRecord.size();i++ ){
                message=(i+1)+") "+message+consumerRecord.get(i)+"\n";
            }
            Logger.logInfo("All Messages recieved in Kaftka consumer :<br> "+message);
        }
    }

    public void logkafkaConsumerMessages(ArrayList<String> consumerRecord){
        if(consumerRecord.size()!=0){
            String message="";
            for (int i=0;i<consumerRecord.size();i++ ){
                message=(i+1)+") "+message+consumerRecord.get(i)+"\n";
            }
            Logger.logInfoInLogger("All Messages recieved in Kaftka consumer are :<br> "+message);
        }
    }

    public void captureSpecifickafkaConsumerMessage(ArrayList<String> consumerRecord,String uniqueKey){
        int i= KafkaListener.getInstance().getIndexofKafkaConsumerMessage(consumerRecord,uniqueKey);
        Logger.logInfo("Message stored in Kaftka consumer :<br> "+consumerRecord.get(i));

    }

    public int getIndexofKafkaConsumerMessage(ArrayList<String> kafkaRecord, String uniqueKey){
        int i;
        for (i=0;i<kafkaRecord.size();i++){
            if(kafkaRecord.get(i).contains(uniqueKey))
                return i;
            if(i==kafkaRecord.size()-1)
                throw new MessageNotFoundInKafkaConsumerException(" Message not found in Kaftka Consumer ");
        }
        throw new MessageNotFoundInKafkaConsumerException(" Message not found in Kaftka Consumer ");
    }

}
