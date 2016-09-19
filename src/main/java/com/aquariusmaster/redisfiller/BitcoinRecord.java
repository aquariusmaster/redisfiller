package com.aquariusmaster.redisfiller;

/**
 * Created by harkonnen on 18.06.16.
 * POJO class. Represent record entry from xml
 */

public class BitcoinRecord {

    private long id;
    private String bitcoin;

    public BitcoinRecord(){}

    public BitcoinRecord(long id, String bitcoin) {
        this.id = id;
        this.bitcoin = bitcoin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(String bitcoin) {
        this.bitcoin = bitcoin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitcoinRecord record = (BitcoinRecord) o;

        if (id != record.id) return false;
        return bitcoin != null ? bitcoin.equals(record.bitcoin) : record.bitcoin == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (bitcoin != null ? bitcoin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BitcoinRecord{" +
                "id=" + id +
                ", bitcoin='" + bitcoin + '\'' +
                '}';
    }
}
