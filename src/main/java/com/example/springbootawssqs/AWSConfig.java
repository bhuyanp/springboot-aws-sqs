package com.example.springbootawssqs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;

@Configuration
public class AWSConfig {

    @Value("${aws.accessKey}")
    String accessKey;

    @Value("${aws.secret}")
    String secret;

    @Bean
    public AwsCredentials awsCredentials(){
        return AwsBasicCredentials.create(accessKey,secret);
    }

}
