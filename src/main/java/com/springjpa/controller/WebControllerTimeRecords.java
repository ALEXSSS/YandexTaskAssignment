package com.springjpa.controller;

import com.springjpa.model.EventRecord;
import com.springjpa.model.TimeRecord;
import com.springjpa.repo.EventRecordRepository;
import com.springjpa.service.TimeWindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//only for imitation's purposes
@Scope("request")
@RestController
public class WebControllerTimeRecords {

    public static final Logger logger =
            LoggerFactory.getLogger(WebControllerTimeRecords.class);


    @Autowired
    EventRecordRepository repository;

    //this is singleton object, on requests will be created always the same bean
    @Autowired
    TimeWindowService timeWindowService;


    //it is'not good practice to use get for supplying data
    //but here it is for convenience of testing without any post query
    //bellow I will provide post method either
    @RequestMapping("/saveinf")
    public String findById(@RequestParam("data") String data) {
        EventRecord record=new EventRecord(data);
        timeWindowService.add(new TimeRecord(record));
        repository.save(record);
        logger.info("Creating EventRecord from GET");
        return "OK";
    }

    @RequestMapping("/getinf")
    public String getInf(@RequestParam("type") String type_str) {
        logger.info("Retrieving the num of TimeRecords from GET");
        try {
            TimeWindowService.WindowsType type = TimeWindowService.WindowsType.
                    valueOf(type_str);
            return (new Long(timeWindowService.getSize(type))).toString();
        }catch (IllegalStateException e){
            return "Write enum correctly";
        }
    }
    //retrieve all data from bd
    @RequestMapping("/findall")
    public String findAll() {
        String result = "";
        for (EventRecord eventRecord : repository.findAll()) {
            result += eventRecord.toString() + "<br>";
        }
        logger.info("findall : {}", result);

        return result;
    }

    @RequestMapping(value = "/sendTimeRecord/", method = RequestMethod.POST)
    public ResponseEntity<?> createRecord(@RequestBody EventRecord record) {
        logger.info("Creating EventRecord from POST : {}", record);
        repository.save(record);
        timeWindowService.add(new TimeRecord(record));
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

}

