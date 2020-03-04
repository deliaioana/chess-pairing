package eu.chessout.v2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.chessout.shared.model.MyPayLoad;

// useful documentation: https://www.baeldung.com/spring-boot-json

@RestController
public class BasicApiController {

    @PutMapping("/api/gameResultUpdated")
    public String gameResultUpdated(@RequestBody MyPayLoad myPayLoad) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String stringPlayLoad = objectMapper.writeValueAsString(myPayLoad);

        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/api/gameResultUpdatedTask").payload(stringPlayLoad));
        System.out.println("Hello gameResultUpdated");
        return "Greetings from gameResultUpdated";
    }

    @PostMapping(
            value = "/api/gameResultUpdatedTask"
            //consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public String gameResultUpdatedTask(@RequestBody String payLoadBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MyPayLoad myPayLoad = objectMapper.readValue(payLoadBody, MyPayLoad.class);
        System.out.println("Hello gameResultUpdatedTask");
        return "Greetings from gameResultUpdatedTask";
    }
}
