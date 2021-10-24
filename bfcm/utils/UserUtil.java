package com.seanco.bfcm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.vo.ResponseInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    private static void createUser(int count) throws Exception {
        System.out.printf("Creating %d users\n", count);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(1234567891L + i);
            user.setNickname("user" + i);
            user.setPassword(MD5Util.inputPasswordToDBPassword("123456", "abc123"));
            user.setSalt("abc123");
            users.add(user);
        }
        System.out.println("Writing to DB");
        Connection conn = getDBConnection();
        String query = "insert into t_user(id,nickname,password,salt) values(?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(query);
        for (User user : users) {
            statement.setLong(1, user.getId());
            statement.setString(2, user.getNickname());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getSalt());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.clearParameters();
        conn.close();

        System.out.println("Getting userTicket values");
        // login, get user ticket
        String url = "http://localhost:8080/login/doLogin";
        File file = new File("/Users/sean/IdeaProjects/bfcm/src/main/resources/users.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(0);
        for (User user : users) {
            URL tmpUtl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) tmpUtl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPasswordToFormPassword("123456");
            outputStream.write(params.getBytes());
            outputStream.flush();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                byteArrayOutputStream.write(buff, 0, len);
            }
            inputStream.close();
            byteArrayOutputStream.close();
            String response = byteArrayOutputStream.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseInfo responseInfo = objectMapper.readValue(response, ResponseInfo.class);
            String userTicket = (String) responseInfo.getObj();
            String row = user.getId() + "," + userTicket;
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(row.getBytes());
            randomAccessFile.write("\r\n".getBytes());
            System.out.println("Created user:" + user.getId());
        }
        randomAccessFile.close();
        System.out.println("Finished!");
    }

    private static Connection getDBConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/bfcm?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username = "root";
        String password = "12345678";
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
