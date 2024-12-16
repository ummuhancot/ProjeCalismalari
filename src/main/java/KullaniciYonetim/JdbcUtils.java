package KullaniciYonetim;

import java.sql.*;

public class JdbcUtils {
    public static Connection connection;
    public static Statement st;
    public static PreparedStatement preparedStatement;

    public static void setConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kullanici_yonetim", "techpro", "password");
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setStatement() {
        try {
            st = connection.createStatement();
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeStatement() {
        try {
            connection.close();
            st.close();
        } catch (
                SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public static void setPreparedStatement(String sql) {
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (
                SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void closePreparedStatement() {
        try {
            connection.close();
            preparedStatement.close();
        } catch (
                SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}