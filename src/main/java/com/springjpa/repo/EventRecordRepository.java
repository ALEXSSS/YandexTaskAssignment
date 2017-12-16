package com.springjpa.repo;

import com.springjpa.model.EventRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by alex on 15.12.17.
 */
public interface EventRecordRepository extends CrudRepository<EventRecord, Long> {
    List<EventRecord> findByData(String data);
}



