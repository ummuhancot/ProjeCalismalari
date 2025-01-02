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
            JdbcUtils.st.execute("CREATE TABLE IF NOT EXISTS t_user (\n" + "id SERIAL PRIMARY KEY,\n" + "name VARCHAR(50) NOT NULL CHECK(LENGTH(name)>0),\n" + "email VARCHAR(255) NOT NULL UNIQUE CHECK(LENGTH(email)>0),\n" + "role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN', 'MODERATOR', 'KULLANICI'))\n" + ")");
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
            JdbcUtils.preparedStatement.setString(3, user.getRole().getRole());
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
                user.setId(userResult.getInt("id"));
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
    public void update(User user) {
        JdbcUtils.setConnection();
        JdbcUtils.setPreparedStatement("UPDATE t_user SET name = ?, email = ?, role = ? WHERE id = ? ");
        try {
            JdbcUtils.preparedStatement.setString(1, user.getName());
            JdbcUtils.preparedStatement.setString(2, user.getEmail());
            JdbcUtils.preparedStatement.setString(3, user.getRole().getRole());
            JdbcUtils.preparedStatement.setInt(4, user.getId());
            int updated = JdbcUtils.preparedStatement.executeUpdate();
            if (updated > 0) {
                System.out.println("Kullanici basariyla güncellendi.");
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closePreparedStatement();
        }
    }

    @Override
    public void deleteById(Integer id) {
        JdbcUtils.setConnection();
        JdbcUtils.setStatement();
        try {
            int deleted = JdbcUtils.st.executeUpdate("DELETE FROM t_user WHERE id = " + id);
            if (deleted > 0) {
                System.out.println("Silme islemi basariyla tamamlandi silinen id " + id);
            } else {
                System.out.println("Silme islemi basarisiz. Verilen id ile kullanici bulunamadi");
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            JdbcUtils.closeStatement();
        }
    }

    @Override
    public User findById(Integer id) {
        User user = null;
        JdbcUtils.setConnection();
        JdbcUtils.setPreparedStatement("SELECT * FROM t_user WHERE id=?");
        try {
            JdbcUtils.preparedStatement.setInt(1, id);
            ResultSet rs = JdbcUtils.preparedStatement.executeQuery();

            if (rs.next()) {
                // role String olarak alınır ve Role enum değerine dönüştürülür
                String roleString = rs.getString("role");
                Role roleEnum;

                try {
                    roleEnum = Role.valueOf(roleString.toUpperCase());
                } catch (
                        IllegalArgumentException e) {
                    throw new RuntimeException("Role enum conversion failed: " + roleString, e);
                }

                // User nesnesi oluşturulur
                user = new User(rs.getString("name"), rs.getString("email"), roleEnum);
                user.setId(rs.getInt("id"));
            }
        } catch (
                SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        } finally {
            JdbcUtils.closePreparedStatement();
        }

        return user;
    }
}