package com.springjpa;

import com.springjpa.repo.EventRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 15.12.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllers {

    @LocalServerPort
    private int port;

    @Autowired
    EventRecordRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/saveinf?data=123",
                String.class)).contains("OK");
    }

    @Test
    public void getShouldSaveData(){
        this.restTemplate.getForObject("http://localhost:" + port + "/saveinf?data=getShouldSaveData",String.class);
        assertTrue(repository.findByData("getShouldSaveData").size()>0);
    }
}