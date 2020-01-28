package com.example.caipiao.service;

import java.sql.*;
import java.util.Random;

public class Choujiang {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Random random = new Random();
//        random.ints(6, 1, 33).sorted().forEach(aa -> System.out.print(aa + " "));aa


        Class.forName("org.apache.drill.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:drill:drillbit=127.0.0.1");
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT * from cp.`employee.json` LIMIT 1");
        while(rs.next()){
            System.out.println(rs.getString(1));
        }

    }
}