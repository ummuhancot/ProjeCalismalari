package KullaniciYonetim;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UserService {

    Scanner inp = Runner.input;

    private Repository repo = new UserRepository();


    public void createUserTable() {
        repo.createTable();
    }

    public User getUserInfo() {
        System.out.println("AD : ");
        String name = inp.nextLine();
        System.out.println("EMAİL : ");
        String email = inp.nextLine();
        System.out.println("ROLE: ");
        String role = inp.nextLine();
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase()); // Enum değerine dönüştürme
        } catch (
                IllegalArgumentException e) {
            System.out.println("Geçersiz rol girdiniz. Lütfen tekrar deneyin.");
            return null; // Hatalı giriş durumunda null dönebilir veya tekrar deneyim için döngüye alabilirsiniz.
        }
        return new User(name, email, roleEnum);
    }


    public void saveUser() {
        System.out.println("AD : ");
        String name = inp.nextLine();
        System.out.println("EMAİL : ");
        String email = inp.nextLine();
        System.out.println("ROLE: ");
        String role = inp.nextLine();
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase()); // Enum değerine dönüştürme
            repo.save(new User(name, email, roleEnum));
        } catch (
                IllegalArgumentException e) {
            System.out.println("Geçersiz rol girdiniz. Lütfen tekrar deneyin."); // Hatalı giriş durumunda null dönebilir veya tekrar deneyim için döngüye alabilirsiniz.
        }
    }

    public User getUserById(int id) {
        User user = (User) repo.findById(id);
        return user;
    }

    public void deleteUserById(int id) {
        repo.deleteById(id);
    }


    public void listAllUser() {
        List<User> allUsers = repo.findAll();
        System.out.println("TÜM KULLANICILAR");
        for (User user : allUsers) {
            System.out.println("id : " + user.getId() + " name : " + user.getName() + " email : " + user.getEmail() + " role : " + user.getRole().getRole());
        }
    }

    public void updateUserBy(int id) {
        User foundUser = getUserById(id);
        if (foundUser == null) {
            System.out.println("Id si verilen kullanici bulunamadi");
        } else {
            User updatedUser = getUserInfo();
            foundUser.setName(updatedUser.getName());
            foundUser.setEmail(updatedUser.getEmail());
            foundUser.setRole(updatedUser.getRole());
            repo.update(foundUser);
        }
    }


    public void generateReport() {

        List<User> allUsers = repo.findAll();
        System.err.println("Rapor Yükleniyor");
        try {
            FileWriter writer = new FileWriter("user__report.txt");
            writer.write("==========User Report==========\n");
            writer.write("--------------------------" + "\n");
            for (User user : allUsers) {

                writer.write("Ad : " + user.getName() + " Email : " + user.getEmail() + " Role : " + user.getRole().getRole() + "\n");

            }
            writer.close();
            System.err.println("Report generated and printed to user_report.txt");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }


    }


}