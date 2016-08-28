package com.aquariusmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harkonnen on 12.07.16.
 */
@Repository
public class BitcoinRecordRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinRecordRepository.class);
    public static final String KEY = "bitcoins:";

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public BitcoinRecord getRecord(long id){

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Trying to get record with id=" + id );
        }
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String bitcoin = values.get(KEY + id);
        BitcoinRecord retrived = new BitcoinRecord(id, bitcoin);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Retrived from record with id = " + id + ": " + retrived );
        }
        return retrived;
    }

    public void saveRecord(BitcoinRecord record){

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Trying to save record: " + record );
        }
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(KEY + record.getId(), record.getBitcoin());
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Saved in Redis: " + record);
        }
    }

    public void saveRecords(List<BitcoinRecord> records){

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Trying to save list of records: " + records );
        }
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.multiSet(recordsListToMap(records));
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Saved List of reconds in Redis: " + records);
        }
    }

    private Map<String, String> recordsListToMap(List<BitcoinRecord> recordList){
        Map<String, String> rawMap = new HashMap<>();
        for (BitcoinRecord rec : recordList){
            rawMap.put(KEY + rec.getId(), rec.getBitcoin());
        }
        return rawMap;
    }
}
