package com.aquariusmaster.redis;

import com.aquariusmaster.ApplicationConfig;
import com.aquariusmaster.BitcoinRecord;
import com.aquariusmaster.BitcoinRecordRepository;
import com.aquariusmaster.RedisfillerApplication;
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
@SpringApplicationConfiguration(classes = {RedisfillerApplication.class, ApplicationConfig.class})
public class BitcoinBitcoinBitcoinRecordRepositoryTest {

    @Autowired
    BitcoinRecordRepository bitcoinRecordRepository;

    @Test
    public void redisReadAndSaveTest(){

        BitcoinRecord rec = new BitcoinRecord(2, "fdghfhfh252rg34tg34");
        bitcoinRecordRepository.saveRecord(rec);
        BitcoinRecord retrived = bitcoinRecordRepository.getRecord(2);
        assertEquals(rec, retrived);
    }

}