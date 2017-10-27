package com.ef.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Record {
    Long id;
    Date date;
    String ip;
    String request;
    String status;
    String userAgent;

    public Record(String record) {
        List<String> aList= new ArrayList<>();
        StringTokenizer stringTokenizer= new StringTokenizer(record,"|");
        while(stringTokenizer.hasMoreTokens()){
            aList.add(stringTokenizer.nextToken());
        }
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            this.setDate(dateFormat.parse(aList.get(0)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setIp(aList.get(1));
        this.setRequest(aList.get(2));
        this.setStatus(aList.get(3));
        this.setUserAgent(aList.get(4));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;

        Record record = (Record) o;

        if (getId() != null ? !getId().equals(record.getId()) : record.getId() != null) return false;
        if (getDate() != null ? !getDate().equals(record.getDate()) : record.getDate() != null) return false;
        if (getIp() != null ? !getIp().equals(record.getIp()) : record.getIp() != null) return false;
        if (getRequest() != null ? !getRequest().equals(record.getRequest()) : record.getRequest() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(record.getStatus()) : record.getStatus() != null) return false;
        return getUserAgent() != null ? getUserAgent().equals(record.getUserAgent()) : record.getUserAgent() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getIp() != null ? getIp().hashCode() : 0);
        result = 31 * result + (getRequest() != null ? getRequest().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getUserAgent() != null ? getUserAgent().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", date=" + date +
                ", ip='" + ip + '\'' +
                ", request='" + request + '\'' +
                ", status='" + status + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }


}
