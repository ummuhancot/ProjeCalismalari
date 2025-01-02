package KullaniciYonetim;

import java.util.Scanner;

public class Runner {

    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    private static void start() {
		/*Konsol Menüsü:
		Kullanıcıya aşağıdaki işlemleri yapabileceği bir menü sunulacak:
		Yeni Kullanıcı Ekle
		Kullanıcıları Listele
		Kullanıcı Bilgilerini Güncelle
		Kullanıcıyı Sil
		Çıkış Yap*/

        UserService userService = new UserService();
        userService.createUserTable();
        int select;
        int id;

        do
        {
            System.out.println("------------------------------------");
            System.out.println("1-Yeni Kullanici Ekle");
            System.out.println("2-Kullanicilari Listele");
            System.out.println("3-Kullanici Bilgilerini Güncelle");
            System.out.println("4-Kullaniciyi Sil");
            System.out.println("5-Kullancinın Rapora yazdırma ");
            System.out.println("0-Cikis");
            select = input.nextInt();
            input.nextLine();

            switch (select) {
                case 1:
                    userService.saveUser();
                    break;
                case 2:
                    userService.listAllUser();
                    break;
                case 3:
                    id=getIdInfo();
                    userService.updateUserBy(id);
                    break;
                case 4:
                    id=getIdInfo();
                    userService.deleteUserById(id);
                    break;
                case 5:
                    userService.generateReport();
                    break;
                case 0:
                default:
                    break;

            }


        } while (select != 0);

    }

    private static int getIdInfo(){
        System.out.println("Id : ");
        int id = input.nextInt();
        input.nextLine();
        return id;
    }


}