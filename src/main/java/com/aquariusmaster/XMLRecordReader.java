package com.aquariusmaster;

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
    private static int counter;

    public static List<Record> readRecordFromXML(int count, String path){

        LOGGER.info("Read record from xml file: " + path);
        LOGGER.info("Counter =  " + counter + ", count of records for filling = " + count);

        int skip = counter;
        List<Record> records = null;

        try {
            Record currentRecord = null;
            String tagContent = null;
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader =
                    reader = factory.createXMLStreamReader(new FileInputStream(path));

            label:
            while(reader.hasNext()){

                if (records != null && records.size() == count) {
                    counter += count;
                    return records;
                }

                int event = reader.next();

                switch(event){
                    case XMLStreamConstants.START_ELEMENT:
                        if ("record".equals(reader.getLocalName())){
                            currentRecord = new Record();
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
                                records.add(currentRecord);
                                break;
                            case "id":
                                currentRecord.setId(Long.parseLong(tagContent));
                                break;
                            case "bitcoin":
                                currentRecord.setBitcoin(tagContent);
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
        counter += count;

        LOGGER.info("Counter =  " + counter + ", count of records for filling = " + count);
        return records;
    }
}
