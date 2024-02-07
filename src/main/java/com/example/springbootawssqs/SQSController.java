package com.example.springbootawssqs;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return sqsService.consumeMessages(MESSAGE_QUEUE_TYPE.STANDARD);
    }

    @GetMapping("/consume/fifo")
    public List<Map<String,Object>> consumeFifo(){
        return sqsService.consumeMessages(MESSAGE_QUEUE_TYPE.FIFO);
    }



}
