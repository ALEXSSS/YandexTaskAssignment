package com.springjpa;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.springjpa.service.TimeWindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//do clean MasterQueue of TimeWindowService depending on type of WindowType
@Component
public class SchedulerCleaner {

    @Autowired
    TimeWindowService timeWindowService;
    private static final Logger log = LoggerFactory.getLogger(SchedulerCleaner.class);

    //do cleaning every 5 seconds for field sec
    @Scheduled(fixedRate = 5 * TimeWindowService.mill_sec_per_sec)
    public void cleanSec() {
        timeWindowService.clean(TimeWindowService.WindowsType.SEC);
        log.info("Sec cleaning");
    }

    @Scheduled(fixedRate = 2 * TimeWindowService.mill_sec_per_min)
    public void cleanMin() {
        timeWindowService.clean(TimeWindowService.WindowsType.MIN);
        log.info("Min cleaning");
    }

    @Scheduled(fixedRate = TimeWindowService.mill_sec_per_hour)
    public void cleanHour() {
        timeWindowService.clean(TimeWindowService.WindowsType.HOUR);
        log.info("Hour cleaning");
    }

    @Scheduled(fixedRate = TimeWindowService.mill_sec_per_hour)
    public void cleanDay() {
        timeWindowService.clean(TimeWindowService.WindowsType.DAY);
        log.info("Day cleaning");
    }
}