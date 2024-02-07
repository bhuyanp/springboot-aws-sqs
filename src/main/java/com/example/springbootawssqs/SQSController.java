package com.example.springbootawssqs;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sqs")
@RequiredArgsConstructor
public class SQSController {
    private final SQSService sqsService;

    @GetMapping("/post/standard")
    public String postStandard(){
        return sqsService.postMessage("Sample message for standard queue. "+new Date(),
                MESSAGE_QUEUE_TYPE.STANDARD);
    }

    @GetMapping("/post/fifo")
    public String postFIFO(){
        return sqsService.postMessage("Sample message for FIFO queue. "+new Date(),
                MESSAGE_QUEUE_TYPE.FIFO);
    }

    @GetMapping("/consume/standard")
    public List<Map<String,Object>> consumeStandard(){
        List<Message> messages = sqsService.consumeMessages(MESSAGE_QUEUE_TYPE.STANDARD);
        return processMessages(messages);
    }

    @GetMapping("/consume/fifo")
    public List<Map<String,Object>> consumeFifo(){
        List<Message> messages = sqsService.consumeMessages(MESSAGE_QUEUE_TYPE.FIFO);
        return processMessages(messages);
    }

    private List<Map<String,Object>> processMessages(List<Message> messages){
        return messages.stream().map(message -> Map.of(
                "messageId", message.messageId(),
                "body", message.body(),
                "attributes", message.messageAttributes()
                        .entrySet().stream().map(attr -> Map.of(
                                "key", attr.getKey(),
                                "value", attr.getValue().stringValue()
                        ))
        )).toList();
    }

}
