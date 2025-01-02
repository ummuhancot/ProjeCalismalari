package KullaniciYonetim;

import java.util.List;

public interface Repository<S, U> {
    void createTable();

    void save(S entity);

    List<S> findAll();

    void update(S entity);

    void deleteById(U id);

    S findById(U id);
}