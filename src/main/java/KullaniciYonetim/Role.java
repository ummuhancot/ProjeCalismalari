package KullaniciYonetim;

public enum Role {
    ADMIN("ADMIN"), MODERATOR("MODERATOR"), KULLANICI("KULLANICI"),
    ;

    Role(String role) {
        this.role = role;
    }

    private final String role;

    public String getRole() {
        return role;
    }
}