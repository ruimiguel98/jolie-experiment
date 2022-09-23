package org.example.bean;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.errors.SerializationException;

import java.util.Map;
import java.nio.ByteBuffer;

public class CreateCheckoutFormSerializer implements Serializer<CreateCheckoutForm> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to configure
    }

    @Override
    public byte[] serialize(String topic, CreateCheckoutForm createCheckoutForm) {

        int sizeOfCheckoutFormStatus;
        int sizeOfCheckoutFormAddress;
        int sizeOfCheckoutFormProducts;

        byte[] serializedCheckoutFormStatus;
        byte[] serializedCheckoutFormAddress;
        byte[] serializedCheckoutFormProducts;

        try {
            if (createCheckoutForm == null)
                return null;

            serializedCheckoutFormStatus = createCheckoutForm.getOrder().getStatus().getBytes(encoding);
            sizeOfCheckoutFormStatus = serializedCheckoutFormStatus.length;

            serializedCheckoutFormAddress = createCheckoutForm.getOrder().getAddressToShip().getBytes(encoding);
            sizeOfCheckoutFormAddress = serializedCheckoutFormAddress.length;

            ByteBuffer buffer = ByteBuffer.allocate(4+4+sizeOfCheckoutFormStatus+4+sizeOfCheckoutFormAddress);

//            buffer.putInt(createCheckoutForm.getEmployeeId());
            buffer.putInt(sizeOfCheckoutFormStatus);
            buffer.put(serializedCheckoutFormStatus);
            buffer.putInt(sizeOfCheckoutFormAddress);
            buffer.put(serializedCheckoutFormAddress);


            return buffer.array();

        } catch (Exception e) {
            throw new SerializationException("Error when serializing CreateCheckoutForm to byte[]");
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, CreateCheckoutForm data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
