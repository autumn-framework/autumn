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

import com.autumn.utils.exceptions.MessageNotFoundInKaftkaConsumerException;
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


public class KaftkaListener {

    private static volatile KaftkaListener instance;

    private KaftkaListener() {
    }

    public static KaftkaListener getInstance() {
        if (instance == null) {
            synchronized (KaftkaListener.class) {
                if (instance == null) {
                    instance = new KaftkaListener();
                }
            }
        }
        return instance;
    }

    public Consumer<String, byte[]> createConsumer(String KaftkaConsumer,String KaftkaTopic,String KaftkaBootstapServer) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KaftkaBootstapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KaftkaConsumer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.setProperty("specific.avro.reader", "true");

        // Create the consumer using props.
        final Consumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(KaftkaTopic));
        Logger.logInfoInLogger("Consumer connection done");
        return consumer;
    }

    public synchronized ArrayList<String> runConsumer(String KaftkaConsumer,String KaftkaTopic,String KaftkaBootstapServer,Schema schema) throws IOException {
        captureKaftkaDetails(KaftkaTopic,KaftkaConsumer,KaftkaBootstapServer);
        Logger.logInfoInLogger("Running Consumer --- " +KaftkaConsumer+" on Topic "+KaftkaTopic);
        ArrayList<String> consumerRecord= new ArrayList<String >();
        final Consumer<String, byte[]> consumer = createConsumer(KaftkaConsumer,KaftkaTopic,KaftkaBootstapServer);
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
        capturekaftkaConsumerMessages(consumerRecord);
        Logger.logInfoInLogger("DONE");
        return consumerRecord;
    }

    public void captureKaftkaDetails(String KaftkaTopic,String KaftkaConsumer,String KaftkaBootstapServer){
        Logger.logInfo("Kaftka details are :<br> Bootstrap Server : "+ KaftkaBootstapServer+"<br>Kaftka Topic : "+KaftkaTopic+"<br>KaftkaConsumer : "+KaftkaConsumer);
    }

    public void capturekaftkaConsumerMessages(ArrayList<String> consumerRecord){
        if(consumerRecord.size()!=0){
            String message="";
            for (int i=0;i<consumerRecord.size();i++ ){
                message=(i+1)+") "+message+consumerRecord.get(i)+"\n";
            }
            Logger.logInfo("All Messages recieved in Kaftka consumer :<br> "+message);
        }
    }

    public void logkaftkaConsumerMessages(ArrayList<String> consumerRecord){
        if(consumerRecord.size()!=0){
            String message="";
            for (int i=0;i<consumerRecord.size();i++ ){
                message=(i+1)+") "+message+consumerRecord.get(i)+"\n";
            }
            Logger.logInfoInLogger("All Messages recieved in Kaftka consumer are :<br> "+message);
        }
    }

    public void captureSpecifickaftkaConsumerMessage(ArrayList<String> consumerRecord,String uniqueKey){
        int i= KaftkaListener.getInstance().getIndexofKaftkaConsumerMessage(consumerRecord,uniqueKey);
        Logger.logInfo("Message stored in Kaftka consumer :<br> "+consumerRecord.get(i));

    }

    public int getIndexofKaftkaConsumerMessage(ArrayList<String> kaftkaRecord, String uniqueKey){
        int i;
        for (i=0;i<kaftkaRecord.size();i++){
            if(kaftkaRecord.get(i).contains(uniqueKey))
                return i;
            if(i==kaftkaRecord.size()-1)
                throw new MessageNotFoundInKaftkaConsumerException(" Message not found in Kaftka Consumer ");
        }
        throw new MessageNotFoundInKaftkaConsumerException(" Message not found in Kaftka Consumer ");
    }

}
