package com.springjpa.service;

import com.springjpa.model.EventRecord;
import com.springjpa.model.TimeRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by alex on 15.12.17.
 */


@Service("TimeWindow")
@Scope("singleton")
public class TimeWindowService {
    private static final Logger log = LoggerFactory.getLogger(TimeWindowService.class);

    private final static long buffer_capacity = 200_000;

    public final static long mill_sec_per_sec = 1000;
    public final static long mill_sec_per_min = mill_sec_per_sec * 60;
    public final static long mill_sec_per_hour = mill_sec_per_min * 60;
    public final static long mill_sec_per_day = mill_sec_per_hour * 24;

//    private ConcurrentLinkedQueue<EventRecord> buffer;
//    private LongAdder ...

    private LinkedList<TimeRecord> buffer;

    public static enum WindowsType {SEC, MIN, HOUR, DAY}

    ;

    public void add(TimeRecord record) {
        log.info("Add TimeService method with current size : {}", getBufferSize());
        synchronized (this) {
            buffer.add(record);
        }
        if (buffer.size() > buffer_capacity) {
            synchronized (this) {
                if (buffer.size() > buffer_capacity) {
                    unsafe_flush();
                }
            }
        }
    }

    public void flush() {
        synchronized (this) {
            unsafe_flush();
        }
    }

    private void unsafe_flush() {
        log.info("Flush TimeService method with current size : {}", getBufferSize());
        for (WindowsType key : windows.keySet()) {
            windows.get(key).flush(buffer);
        }
        buffer = new LinkedList<>();
    }

    private HashMap<WindowsType, QueueMaster> windows;

    public TimeWindowService() {
        buffer = new LinkedList<>();
        windows = new HashMap<>();
        windows.put(WindowsType.SEC, new QueueMaster(mill_sec_per_sec));
        windows.put(WindowsType.MIN, new QueueMaster(mill_sec_per_min));
        windows.put(WindowsType.HOUR, new QueueMaster(mill_sec_per_hour));
        windows.put(WindowsType.DAY, new QueueMaster(mill_sec_per_day));
    }

    public void clean(WindowsType type_of_window) {
        switch (type_of_window) {
            case SEC:
                windows.get(WindowsType.SEC).clean();
                return;
            case MIN:
                windows.get(WindowsType.MIN).clean();
                return;
            case HOUR:
                windows.get(WindowsType.HOUR).clean();
                return;
            case DAY:
                windows.get(WindowsType.DAY).clean();
                return;
        }
        throw new IllegalStateException("Yoy shouldn't be here!, something wrong with enum");
    }

    public long getSize(WindowsType type_of_window) {

        switch (type_of_window) {
            case SEC:
                synchronized (this) {
                    unsafe_flush();
                    return windows.get(WindowsType.SEC).getSize();
                }
            case MIN:
                synchronized (this) {
                    unsafe_flush();
                    return windows.get(WindowsType.MIN).getSize();
                }
            case HOUR:
                synchronized (this) {
                    unsafe_flush();
                    return windows.get(WindowsType.HOUR).getSize();
                }
            case DAY:
                synchronized (this) {
                    unsafe_flush();
                    return windows.get(WindowsType.DAY).getSize();
                }
        }

        throw new IllegalStateException("Yoy shouldn't be here!, something wrong with enum");
    }

    public int getBufferSize() {
        return buffer.size();
    }
}
