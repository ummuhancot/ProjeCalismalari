package KullaniciYonetim;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User, Integer> {


    @Override
    public void createTable() {
        try {
            JdbcUtils.setConnection();
            JdbcUtils.setStatement();


            JdbcUtils.st.execute("CREATE TABLE IF NOT EXISTS t_user (" + "id INT IDENTITY PRIMARY KEY,\n " + "name NVARCHAR(50) NOT NULL CHECK(LEN(name) > 0),\n " + "email NVARCHAR(255) NOT NULL CHECK(LEN(email) > 0),\n " + "role NVARCHAR(20) NOT NULL CHECK(role IN ('ADMIN', 'MODERATOR', 'KULLANICI') )");

            System.out.println("Table created successfully!");

        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeStatement();
        }
    }

    @Override
    public void save(User user) {
        JdbcUtils.setConnection();
        JdbcUtils.setPreparedStatement("INSERT INTO t_user(name, email, role) VALUES(?, ?, ?)");
        try {
            JdbcUtils.preparedStatement.setString(1, user.getName());
            JdbcUtils.preparedStatement.setString(2, user.getEmail());
            JdbcUtils.preparedStatement.setString(3, String.valueOf(user.getRole()));
            JdbcUtils.preparedStatement.executeUpdate();
            System.out.println("Kullanici basariyla kaydedildi.");
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closePreparedStatement();
        }

    }

    @Override
    public List<User> findAll() {
        JdbcUtils.setConnection();
        JdbcUtils.setStatement();
        List<User> users = new ArrayList<>();
        try {
            ResultSet userResult = JdbcUtils.st.executeQuery("SELECT * FROM t_user");
            while (userResult.next()) {
                String role = userResult.getString("role");
                Role roleEnum = Role.valueOf(role.toUpperCase());
                User user = new User(userResult.getString("name"), userResult.getString("email"), roleEnum);
                users.add(user);
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            JdbcUtils.closeStatement();
        }
        return users;
    }

    @Override
    public void update(User entity) {


    }

    @Override
    public void deleteById(Integer id) {

    }


}