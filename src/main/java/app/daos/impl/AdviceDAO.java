package app.daos.impl;

import app.daos.IDAO;
import app.dtos.AdviceDTO;
import app.entities.Advice;
import app.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    public AdviceDTO read(Integer id){
        try (EntityManager em = emf.createEntityManager()){
            Advice advice = em.find(Advice.class, id);
            if  (advice != null) {
                return new AdviceDTO(advice);
            } else {
                return null;
            }
        }
    }

    @Override
    public List<AdviceDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()){
            TypedQuery<AdviceDTO> query = em.createQuery("SELECT new app.dtos.AdviceDTO(a) FROM Advice a", AdviceDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public AdviceDTO create(AdviceDTO adviceDTO) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Advice advice = new Advice(adviceDTO);
            em.persist(advice);
            em.getTransaction().commit();
            return new AdviceDTO(advice);
        }
    }

    public List<AdviceDTO> createMultiple(AdviceDTO[] adviceDTOS)   {
        List<AdviceDTO> adviceDTOList = new ArrayList<>();
        for (AdviceDTO adviceDTO : adviceDTOS)  {
            adviceDTOList.add(create(adviceDTO));
        }
        return adviceDTOList;
    }

    @Override
    public AdviceDTO update(Integer integer, AdviceDTO adviceDTO) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Advice a = em.find(Advice.class, integer);
            a.setAdviceText(adviceDTO.getAdviceText());
            a.setRating(adviceDTO.getRating());
            a.setCategory(adviceDTO.getCategory());
            Advice updatedAdvice = em.merge(a);
            em.getTransaction().commit();
            return updatedAdvice != null ? new AdviceDTO(updatedAdvice) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Advice advice = em.find(Advice.class, integer);
            if (advice != null) {
                em.remove(advice);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()){
            Advice advice = em.find(Advice.class, integer);
            return advice != null;
        }
    }

    public void populate()  {
        // Sample advices
        AdviceDTO[] advices = {
                new AdviceDTO(null, "Advice 1", 5, Category.Health),
                new AdviceDTO(null, "Advice 2", 4, Category.Finance),
                new AdviceDTO(null, "Advice 3", 3, Category.Personal_Development),
        };
        createMultiple(advices);
    }
}
