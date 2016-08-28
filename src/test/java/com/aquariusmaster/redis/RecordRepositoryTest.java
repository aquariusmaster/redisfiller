package com.aquariusmaster.redis;

import com.aquariusmaster.ApplicationConfig;
import com.aquariusmaster.RecordRepository;
import com.aquariusmaster.SimpleredisApplication;
import com.aquariusmaster.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by harkonnen on 13.07.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SimpleredisApplication.class, ApplicationConfig.class})
public class RecordRepositoryTest {

    @Autowired
    RecordRepository recordRepository;

    @Test
    public void redisReadAndSaveTest(){

        Record rec = new Record(2, "fdghfhfh252rg34tg34");
        recordRepository.saveRecord(rec);
        Record retrived = recordRepository.getRecord(2);
        assertEquals(rec, retrived);
    }

}