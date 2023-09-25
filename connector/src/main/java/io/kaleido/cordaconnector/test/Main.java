package io.kaleido.cordaconnector.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.kaleido.firefly.cordapp.metadata.RemittanceInformation;

import java.time.Instant;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
//        RemittanceInformation remittanceInformation = new RemittanceInformation();
//        remittanceInformation.setNarratives(Arrays.asList("Test"));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        String str = mapper.writeValueAsString(remittanceInformation);

        Instant now = Instant.parse("2023-05-19T11:59:25.681Z");
        System.out.println("now: " + now);
    }
}

