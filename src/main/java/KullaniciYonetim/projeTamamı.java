package KullaniciYonetim;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class projeTamamı {


    public class KullaniciYonetimSistemi {

        private static final String DB_URL = "jdbc:postgresql://localhost:5432/kullanici_sistemi";
        private static final String DB_USER = "postgres";
        private static final String DB_PASSWORD = "password";

        public static void main(String[] args) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                createTable(conn);

                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("\n--- Kullanıcı Yönetim Sistemi ---");
                    System.out.println("1. Yeni Kullanıcı Ekle");
                    System.out.println("2. Kullanıcıları Listele");
                    System.out.println("3. Kullanıcı Bilgilerini Güncelle");
                    System.out.println("4. Kullanıcıyı Sil");
                    System.out.println("5. Çıkış Yap");
                    System.out.print("Seçiminiz: ");

                    int secim = scanner.nextInt();
                    scanner.nextLine(); // newline karakterini temizle

                    switch (secim) {
                        case 1 -> addUser(conn, scanner);
                        case 2 -> listUsers(conn);
                        case 3 -> updateUser(conn, scanner);
                        case 4 -> deleteUser(conn, scanner);
                        case 5 -> {
                            System.out.println("Programdan çıkılıyor...");
                            return;
                        }
                        default -> System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Veritabanı bağlantı hatası: " + e.getMessage());
            }
        }

        private static void createTable(Connection conn) throws SQLException {
            String sql = "CREATE TABLE IF NOT EXISTS t_user (\n" +
                    "id VARCHAR(50) PRIMARY KEY,\n" +
                    "ad VARCHAR(50) NOT NULL,\n" +
                    "email VARCHAR(255) NOT NULL UNIQUE,\n" +
                    "rol VARCHAR(20) NOT NULL CHECK(rol IN ('ADMIN', 'MODERATOR', 'KULLANICI'))\n" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tablo başarıyla oluşturuldu veya zaten mevcut.");
            }
        }

        private static void addUser(Connection conn, Scanner scanner) {
            System.out.print("Kullanıcı ID: ");
            String id = scanner.nextLine();
            System.out.print("Kullanıcı Adı: ");
            String ad = scanner.nextLine();
            System.out.print("Kullanıcı Email: ");
            String email = scanner.nextLine();
            System.out.print("Kullanıcı Rolü (ADMIN, MODERATOR, KULLANICI): ");
            String rol = scanner.nextLine().toUpperCase();

            if (!isValidRole(rol)) {
                System.out.println("Geçersiz rol. İşlem iptal edildi.");
                return;
            }

            String sql = "INSERT INTO t_user (id, ad, email, rol) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, ad);
                pstmt.setString(3, email);
                pstmt.setString(4, rol);
                pstmt.executeUpdate();
                System.out.println("Kullanıcı başarıyla eklendi.");
            } catch (SQLException e) {
                System.err.println("Kullanıcı ekleme hatası: " + e.getMessage());
            }
        }

        private static void listUsers(Connection conn) {
            String sql = "SELECT * FROM t_user";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                System.out.println("\n--- Kullanıcılar ---");
                while (rs.next()) {
                    System.out.printf("ID: %s, Ad: %s, Email: %s, Rol: %s\n",
                            rs.getString("id"), rs.getString("ad"), rs.getString("email"), rs.getString("rol"));
                }
            } catch (SQLException e) {
                System.err.println("Kullanıcı listeleme hatası: " + e.getMessage());
            }
        }

        private static void updateUser(Connection conn, Scanner scanner) {
            System.out.print("Güncellenecek Kullanıcı ID: ");
            String id = scanner.nextLine();

            System.out.print("Yeni Kullanıcı Adı (boş bırak: değişiklik yok): ");
            String ad = scanner.nextLine();
            System.out.print("Yeni Kullanıcı Email (boş bırak: değişiklik yok): ");
            String email = scanner.nextLine();
            System.out.print("Yeni Kullanıcı Rolü (ADMIN, MODERATOR, KULLANICI, boş bırak: değişiklik yok): ");
            String rol = scanner.nextLine().toUpperCase();

            if (!rol.isEmpty() && !isValidRole(rol)) {
                System.out.println("Geçersiz rol. İşlem iptal edildi.");
                return;
            }

            StringBuilder sql = new StringBuilder("UPDATE t_user SET ");
            List<Object> params = new ArrayList<>();

            if (!ad.isEmpty()) {
                sql.append("ad = ?, ");
                params.add(ad);
            }
            if (!email.isEmpty()) {
                sql.append("email = ?, ");
                params.add(email);
            }
            if (!rol.isEmpty()) {
                sql.append("rol = ?, ");
                params.add(rol);
            }

            if (params.isEmpty()) {
                System.out.println("Hiçbir değişiklik yapılmadı.");
                return;
            }

            sql.setLength(sql.length() - 2); // Son virgülü kaldır
            sql.append(" WHERE id = ?");
            params.add(id);

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Kullanıcı başarıyla güncellendi.");
                } else {
                    System.out.println("Kullanıcı bulunamadı.");
                }
            } catch (SQLException e) {
                System.err.println("Kullanıcı güncelleme hatası: " + e.getMessage());
            }
        }

        private static void deleteUser(Connection conn, Scanner scanner) {
            System.out.print("Silinecek Kullanıcı ID: ");
            String id = scanner.nextLine();

            String sql = "DELETE FROM t_user WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Kullanıcı başarıyla silindi.");
                } else {
                    System.out.println("Kullanıcı bulunamadı.");
                }
            } catch (SQLException e) {
                System.err.println("Kullanıcı silme hatası: " + e.getMessage());
            }
        }

        private static boolean isValidRole(String role) {
            return role.equals("ADMIN") || role.equals("MODERATOR") || role.equals("KULLANICI");
        }
    }

}
