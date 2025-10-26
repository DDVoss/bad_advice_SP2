package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.AdviceDAO;
import app.dtos.AdviceDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class AdviceController implements IController<AdviceDTO, Integer> {

    private final AdviceDAO dao;

    public AdviceController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AdviceDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();

        // DTO
        AdviceDTO adviceDTO = dao.read(id);

        // Response
        ctx.res().setStatus(200);
        ctx.json(adviceDTO, AdviceDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOs
        List<AdviceDTO> adviceDTOS = dao.readAll();
        // Response
        ctx.res().setStatus(200);
        ctx.json(adviceDTOS, List.class);
    }

    @Override
    public void create(Context ctx) {
        // Request
        AdviceDTO jsonRequest = ctx.bodyAsClass(AdviceDTO.class);
        // DTO
        AdviceDTO createdAdviceDTO = dao.create(jsonRequest);
        // Response
        ctx.res().setStatus(201);
        ctx.json(createdAdviceDTO, AdviceDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        AdviceDTO adviceDTO = dao.update(id, validateEntity(ctx));
        // Response
        ctx.res().setStatus(200);
        ctx.json(adviceDTO, AdviceDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        // Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        dao.delete(id);
        // Response
        ctx.res().setStatus(204);
        ctx.json("{ \"message\": \"Advice deleted successfully\" }");
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public AdviceDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(AdviceDTO.class)
                .check(a -> a.getAdviceText() != null && !a.getAdviceText().isEmpty(), "Advice text must not be null or empty")
                .check(a -> a.getRating() != null && a.getRating() >= 1 && a.getRating() <= 10, "Rating must be between 1 and 10")
                .check(a -> a.getCategory() != null, "Category must not be null or empty")
                .get();
    }

    public void populate(Context ctx) {
        dao.populate();
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Database has been populated\" }");
    }

    public void wipeAdvices(Context ctx) {
        dao.clear();
        ctx.res().setStatus(204);
        ctx.json("{ \"message\": \"Database has been wiped\" }");
    }

    public void getRandomAdvice(Context ctx)  {
        AdviceDTO randomAdvice = dao.readRandom();
        if (randomAdvice != null) {
            ctx.res().setStatus(200);
            ctx.json(randomAdvice, AdviceDTO.class);
        } else {
            ctx.res().setStatus(404);
            ctx.json("{ \"Status: 404\", \" message\": \" No advice found\" }");
        }

    }


}
