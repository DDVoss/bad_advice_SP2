package app.daos.impl;

import app.daos.IDAO;
import app.dtos.AdviceDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AdviceDAO implements IDAO<AdviceDTO, Integer> {

    private static AdviceDAO instance;
    private static EntityManagerFactory emf;

    public static AdviceDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdviceDAO();
        }
        return instance;
    }

    @Override
    public AdviceDTO read(Integer integer) {
        return null;
    }

    @Override
    public List<AdviceDTO> readAll() {
        return List.of();
    }

    @Override
    public AdviceDTO create(AdviceDTO adviceDTO) {
        return null;
    }

    @Override
    public AdviceDTO update(Integer integer, AdviceDTO adviceDTO) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }
}
