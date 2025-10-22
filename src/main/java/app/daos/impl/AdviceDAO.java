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

    AdviceDTO addAdvice(String adviceText, int rating, Category category)   {
        return new AdviceDTO(null, adviceText, rating, category);
    }

    public void populate()  {
        // Sample advices
        AdviceDTO[] advices = {
                addAdvice("Allow your expectations to grow faster than your income", 6, Category.Finance),
                addAdvice("Envy others’ success without having a full picture of their lives", 3, Category.Career),
                addAdvice("Pursue status at the expense of independence", 8, Category.Career),
                addAdvice("Associate net worth with self-worth (for you and others)", 7, Category.Personal_Development),
                addAdvice("Mimic the strategy of people who want something different than you do", 6, Category.Personal_Development),
                addAdvice("Choose who to trust based on follower count", 9, Category.Personal_Development),
                addAdvice("Associate engagement with insight", 5, Category.Relationships),
                addAdvice("Let envy guide your goals", 3, Category.Personal_Development),
                addAdvice("Automatically associate wealth with wisdom.", 5, Category.Career),
                addAdvice("Assume that people who disagree with you are uninformed or biased.", 4, Category.Relationships),
                addAdvice("Assume a new dopamine hit is a good indication of long-term joy", 8, Category.Personal_Development),
                addAdvice("Equate busyness with productivity", 2, Category.Career),
                addAdvice("View every conversation as a competition to win", 8, Category.Relationships),
                addAdvice("Assume people care where you went to school after age 25", 6, Category.Career),
                addAdvice("Assume the solution to all your problems is more money", 4, Category.Finance),
                addAdvice("Treat feedback as a personal attack", 7, Category.Personal_Development),
                addAdvice("Maximize efficiency in a way that leaves no room for error", 9, Category.Career),
                addAdvice("Be transactional vs. relationship driven", 10, Category.Relationships),
                addAdvice("Prioritize defending what you already believe over learning something new", 8, Category.Personal_Development),
                addAdvice("Assume that what people can communicate is 100% of what they know or believe", 2, Category.Relationships),
                addAdvice("Believe that the past was golden, the present is crazy, and the future is destined for decline." , 5, Category.Personal_Development),
                addAdvice("Assume that all your success is due to hard work and all your failure is due to bad luck", 4, Category.Career),
                addAdvice("Forecast with precision, certainty, and confidence", 4, Category.Personal_Development),
                addAdvice("Maximize for immediate applause over long-term reputation", 6, Category.Career),
                addAdvice("Value the appearance of looking busy", 3, Category.Career),
                addAdvice("Never doubt your tribe but be skeptical of everyone else’s", 7, Category.Relationships),
                addAdvice("Assume effort is rewarded more than results", 5, Category.Career),
                addAdvice("Believe that your nostalgia is accurate", 6, Category.Personal_Development),
                addAdvice("Compare your behind-the-scenes life to others’ curated highlight reel", 8 , Category.Personal_Development),
                addAdvice("Discount adaptation, assuming every problem will persist and every advantage will remain", 9, Category.Career),
                addAdvice("Use uncertainty as an excuse for inaction", 7, Category.Personal_Development),
                addAdvice("Judge other people at their worst and yourself at your best", 6, Category.Relationships),
                addAdvice("Assume learning is complete upon your last day of school", 4, Category.Career),
                addAdvice("Invest in things that make you feel good in the moment but don’t pay off later", 5, Category.Finance),
                addAdvice("Allways invest more than you spend", 10, Category.Finance),
                addAdvice("Allways invest in bullmarkets at its likely peak", 8, Category.Finance),
                addAdvice("Meme coins are a safe investment", 7, Category.Finance),
                addAdvice("View patience as laziness", 6, Category.Career),
                addAdvice("Be tribal, view everything as a battle for social hierarchy", 7, Category.Relationships),
                addAdvice("Make friends with people whose morals you know are beneath your own", 8, Category.Relationships),
                addAdvice("To get over anxiety, just calm down a bit", 6, Category.Health)
        };
        createMultiple(advices);
    }

    public void clear() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Advice").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE advice RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    public AdviceDTO readRandom() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Advice> query = em.createQuery(
                    "SELECT a FROM Advice a ORDER BY RANDOM()",
                    Advice.class
            );
            query.setMaxResults(1);
            List<Advice> results = query.getResultList();
            return results.isEmpty() ? null : new AdviceDTO(results.get(0));
        }
    }

}
