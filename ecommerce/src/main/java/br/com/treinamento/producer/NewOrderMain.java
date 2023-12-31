package br.com.treinamento.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties());

        var value = "1, 123, 6656454489";
        var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);

        var emailMessage = "Thank you for your order! we are processing your order!";
        var emailRecord = new ProducerRecord<>("ECOMMERCE_NEW_EMAIL", emailMessage, emailMessage);

        Callback callback = getCallback();
        producer.send(record, callback).get();
        producer.send(emailRecord, callback).get();
    }

    private static Properties properties() {
        var properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }

    private static Callback getCallback() {
        return (data, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviado " + data.topic() + " :::partition " + data.partition() +
                    " / offset " + data.offset() + " / timestamp " + data.timestamp());
        };
    }
}