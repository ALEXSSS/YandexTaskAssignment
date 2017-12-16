package com.springjpa;


import com.springjpa.model.EventRecord;
import com.springjpa.model.TimeRecord;
import com.springjpa.service.TimeWindowService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.support.membermodification.MemberModifier;

import java.util.LinkedList;
import java.util.List;


public class TestTimeWindowService {
    TimeWindowService timeWindowService;

    @Before
    public void init() {
        timeWindowService = new TimeWindowService();
    }

    @Test
    public void checkBufferCapacityBehavior() throws IllegalAccessException {
        List<EventRecord> buffer = new LinkedList<>();
        MemberModifier.field(TimeWindowService.class, "buffer").
                set(timeWindowService, buffer);
        for (int i = 0; i < 200_000; i++) {
            timeWindowService.add(new TimeRecord(
                    new EventRecord(Integer.valueOf(i).toString())));
        }
        Assert.assertEquals("size should be the same", 200_000, timeWindowService.getBufferSize());
        timeWindowService.add(new TimeRecord(new EventRecord(Integer.valueOf(200_001).toString())));
        Assert.assertEquals("size should be the same", 0, timeWindowService.getBufferSize());
        timeWindowService.add(new TimeRecord(new EventRecord(Integer.valueOf(1).toString())));
        timeWindowService.add(new TimeRecord(new EventRecord(Integer.valueOf(1).toString())));
        //check that buffer reference has changed
        Assert.assertEquals("size should be on one bigger than capacity", 200_001, buffer.size());
    }
}