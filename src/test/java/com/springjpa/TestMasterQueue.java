package com.springjpa;

import com.springjpa.model.EventRecord;
import com.springjpa.model.TimeRecord;
import com.springjpa.service.QueueMaster;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

/**
 * Created by alex on 16.12.17.
 */
public class TestMasterQueue {
    @Test
    public void queueMasterClean() {
        QueueMaster queueMaster = Mockito.spy(new QueueMaster(10));
        doReturn(200L).when(queueMaster).getTime();
        for (int i = 0; i < 100; i++) {
            TimeRecord time = new TimeRecord(new EventRecord(new Integer(i).toString()));
            time.setTime((long) (i * 2));
            queueMaster.add(time);
        }
        queueMaster.clean();
        Assert.assertEquals("Expected 5", 5L, queueMaster.getSize());
    }

    @Test
    public void queueMasterFlush() {
        QueueMaster queueMaster = Mockito.spy(new QueueMaster(10));
        doReturn(200L).when(queueMaster).getTime();
        ArrayList<TimeRecord> buffer = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            TimeRecord time = new TimeRecord(new EventRecord(new Integer(i).toString()));
            time.setTime((long) (i * 2));
            buffer.add(time);
        }
        queueMaster.flush(buffer);
        Assert.assertEquals("Expected 5", 5L, queueMaster.getSize());
    }

    @Test
    public void queueMultiThreadingTest() throws InterruptedException{
        QueueMaster queueMaster = Mockito.spy(new QueueMaster(10));
        doReturn(200L).when(queueMaster).getTime();
        List<Thread> executionPool=new ArrayList<>();

        for (int thread_num = 0; thread_num < 5; thread_num++) {
            executionPool.add(new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    TimeRecord time = new TimeRecord(new EventRecord(new Integer(i).toString()));
                    time.setTime((long) (i * 2));
                    queueMaster.add(time);
                }
            }));
            executionPool.add(new Thread(() -> queueMaster.clean()));
        }
        executionPool.forEach(thread -> thread.start());
        for(Thread thread : executionPool){
            thread.join();
        }
        Assert.assertEquals("Expected 25", 25L, queueMaster.getSize());
    }
}
