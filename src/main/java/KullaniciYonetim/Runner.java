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

        int select;


        do
        {
            System.out.println("------------------------------------");
            System.out.println("1-Yeni Kullanici Ekle");
            System.out.println("2-Kullanicilari Listele");
            System.out.println("3-Kullanici Bilgilerini Güncelle");
            System.out.println("4-Kullaniciyi Sil");
            System.out.println("0-Cikis");
            select = input.nextInt();
            input.nextLine();

            switch (select) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 0:
                    break;
                default:
                    break;

            }


        } while (select != 0);

    }




}