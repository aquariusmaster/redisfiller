package com.aquariusmaster.redisfiller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harkonnen on 13.07.16.
 */
public class XMLRecordReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLRecordReader.class);


    public static List<BitcoinRecord> readRecordFromXML(String path, int limit, int skip){

        LOGGER.info("Read record from xml file: " + path + ". Skip: " + skip + ". Limit: " + limit + ".");

        List<BitcoinRecord> records = null;
        BitcoinRecord currentRecord = null;
        String tagContent = "";
        try {

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader =
                    reader = factory.createXMLStreamReader(new FileInputStream(path));

            label:
            while(reader.hasNext()){

                if (records != null && records.size() == limit) {
                    return records;
                }

                int event = reader.next();

                switch(event){
                    case XMLStreamConstants.START_ELEMENT:
                        if ("record".equals(reader.getLocalName())){
                            currentRecord = new BitcoinRecord();
                        }
                        if("dataset".equals(reader.getLocalName())){
                            records = new ArrayList<>();
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch(reader.getLocalName()){
                            case "record":
                                if (skip-- > 0) {
                                    continue label;
                                }
                                if (records != null) {
                                    records.add(currentRecord);
                                }
                                break;
                            case "id":
                                if (currentRecord != null) {
                                    currentRecord.setId(Long.parseLong(tagContent));
                                }
                                break;
                            case "bitcoin":
                                if (currentRecord != null) {
                                    currentRecord.setBitcoin(tagContent);
                                }
                                break;
                        }
                        break;

                    case XMLStreamConstants.START_DOCUMENT:
                        records = new ArrayList<>();
                        break;
                }

            }

        } catch (XMLStreamException e) {
            LOGGER.error("XML parsing error");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found");
            e.printStackTrace();
        }
        return records;
    }
}
