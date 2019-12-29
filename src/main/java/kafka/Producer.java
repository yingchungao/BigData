package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer extends Thread{

    private final KafkaProducer<Integer, String> producer;
    private String topic = "";
    public Producer(String topic) {
        this.topic = topic;
        Properties prop = new Properties();

        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.CLIENT_ID_CONFIG, "ming");
        prop.put(ProducerConfig.LINGER_MS_CONFIG, "111");
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, "111111111111111111");
        producer = new KafkaProducer<Integer, String>(prop);
    }

    @Override
    public void run() {
        super.run();
        int num = 0;
        while (num < 50) {
            String msg = "pratice test message:" + num;
            try {
                producer.send(new ProducerRecord<Integer, String>(topic, msg)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
