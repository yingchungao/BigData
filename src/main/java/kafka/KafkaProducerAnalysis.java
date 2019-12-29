package kafka;

import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerAnalysis {

    public static final String brokerlist = "localhost:9092";
    public static final String topic = "topic-demo";

    public static Properties initConfig() {
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerlist);
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.CLIENT_ID_CONFIG, "client-id");
        return prop;
    }

    public static void main(String[] args) {
        Properties prop = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "message: " + i);
            producer.send(record);
        }



    }

}
