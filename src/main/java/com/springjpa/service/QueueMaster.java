package com.springjpa.service;

import com.springjpa.model.TimeRecord;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by alex on 15.12.17.
 */
public class QueueMaster {
    private long window;
    private LinkedList<TimeRecord> queue;

    public QueueMaster(long window) {
        queue = new LinkedList<>();
        this.window = window;
    }

    public void add(TimeRecord timeRecord) {
        synchronized (this) {
            queue.add(timeRecord);
        }
    }


    public void flush(Collection<TimeRecord> collection) {
        synchronized (this) {
            queue.addAll(collection);
        }
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    private void clear_expired_records() {
        long current_time = getTime();
        for (Iterator<TimeRecord> iter = queue.iterator(); iter.hasNext(); ) {
            TimeRecord timeRecord = iter.next();
            if (current_time - timeRecord.getTime() > window) {
                iter.remove();
            }
        }
    }

    public void clean() {
        synchronized (this) {
            clear_expired_records();
        }
    }

    public long getSize() {
        synchronized (this) {
            clear_expired_records();
            return queue.size();
        }
    }
}

