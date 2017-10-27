package com.ef;

import com.ef.model.Record;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;


public class Parser {
    private String query="insert into logrecords (request_date,ip_address,request_type,status,user_agent)"
            +" values (?,?,?,?,?) ";
    private String findQuery="select ip_address from logrecords where request_date>=? and request_date < ? group by ip_address having count(ip_address) >= ?";
    private Connection connection;
    private final int batchSize=1000;
    private int count=0;
    private PreparedStatement statement;

    public static void main(String ... args){
        if(args.length !=4){
            System.out.println("Please provide valid number of program agruments: ");
            System.out.println("Usage: java -cp \"parser.jar\" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");
            System.out.println("Program Exiting");
            System.exit(1);
        }else {
            String accessLog = args[0].substring(args[0].lastIndexOf("=")+1);
            String startDate = args[1].substring(args[1].lastIndexOf("=")+1);
            String duration = args[2].substring(args[2].lastIndexOf("=")+1);
            String threshold = args[3].substring(args[3].lastIndexOf("=")+1);
            System.out.println("accesslog: "+accessLog+" startDate: "+startDate+" duration: "+duration+" threshold: "+threshold);
            Parser parser = new Parser();
            //parser.readFile(accessLog);
            parser.fetchRecordsForDuration(startDate,duration,threshold);
            System.out.println("Thank you for using program");
        }
    }

    private void readFile(String pathOfFile) {
        System.out.println("Program is loading records into database");
        try {
            createConnection();
            statement = connection.prepareStatement(query);
            Stream<String> lines = Files.lines(Paths.get(pathOfFile));
            lines.forEach(this::saveRecord);
            System.out.println("Adding remaining records into database, Please wait ...");
            statement.executeBatch();
            lines.close();
            statement.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("All records loaded into database");
    }

    private void saveRecord(String s) {
        Record record = new Record(s);
        try{
            statement.setTimestamp(1, new Timestamp(record.getDate().getTime()));
            statement.setString(2,record.getIp());
            statement.setString(3,record.getRequest());
            statement.setString(4,record.getStatus());
            statement.setString(5,record.getUserAgent());
            statement.addBatch();
            if(++count%batchSize==0) {
                System.out.println("Adding "+count+" records into database");
                statement.executeBatch();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        String url="jdbc:mysql://localhost:3306/parser";
        connection = DriverManager.getConnection(url,"root","root");
    }


    private void fetchRecordsForDuration(String startDate, String duration, String threshold){
        System.out.println("Finding result for desired query");
        java.util.Date sDate = null;
        java.util.Date eDate;
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        List<String> ipAddress = null;
        try {
            sDate = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(sDate);

        if(duration.equals("hourly")){
            calendar.add(Calendar.HOUR_OF_DAY,1);
        }
        else if (duration.equals("daily")) {
            calendar.add(calendar.HOUR_OF_DAY, 24);
        } else {
            System.out.println("Entered duration is wrong, null will be returned");
        }

        eDate = calendar.getTime();
        System.out.println("running query to find records with startDate: "+sDate+" end date "+eDate+" threshold: "+threshold);
        ipAddress= getRecordForDuration(sDate,eDate,threshold);
        for (String record:ipAddress) {
            System.out.println(record);
        }
    }

    private List<String> getRecordForDuration(java.util.Date sDate, java.util.Date eDate, String threshold) {
        PreparedStatement preparedStatement = null;
        List<String> ipAdress = new ArrayList<>();
        try {
            createConnection();
            preparedStatement = connection.prepareStatement(findQuery);
            preparedStatement.setTimestamp(1, new Timestamp(sDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(eDate.getTime()));
            preparedStatement.setString(3,threshold);
            System.out.println("Query executing "+findQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ipAdress.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return ipAdress;
    }
}
