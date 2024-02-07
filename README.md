## Sample SpringBoot Application for AWS SQS


### Getting Started
Visit AWS SQS console and create standard and FIFO queues. Copy their URLs and populate the following application properties.

```
aws.sqs.queue.standard.url=<Standard URL><br/>
aws.sqs.queue.fifo.url=<FIFO URL>
```

Create credential for the test user(never use the root user for this).
Copy the accessKey and secret and populate the following application properties.
```
aws.accessKey=<Access key>
aws.secret=<Secret>
```

Note: Make sure that this user(or the group it belongs to) has AmazonSQSFullAccess policy attached to it.

### Launch Application

Open the terminal and issue following command
```
mvn spring-boot:run 
```


### Post Messages 
Use the following URLs to post messages to AWS SQS
#### Standard Queue
http://localhost:8080/api/v1/sqs/post/standard
#### FIFO Queue
http://localhost:8080/api/v1/sqs/post/fifo


### Consume Messages
Use the following URLs to consume messages from AWS SQS
#### Standard Queue
http://localhost:8080/api/v1/sqs/consume/standard
#### FIFO Queue
http://localhost:8080/api/v1/sqs/consume/fifo