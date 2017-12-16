package com.springjpa.model;




public class TimeRecord{

    private long id;

    private Long time;

    public TimeRecord(EventRecord eventRecord) {
        this.time = eventRecord.getTime();
        this.id = eventRecord.getId();
    }

    @Override
    public String toString() {
        return String.format("TimeRecorder[id=%d, time='%s']", id, time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeRecord that = (TimeRecord) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
