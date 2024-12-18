package dat.controller;

import dat.dao.ExerciseDAO;
import dat.entities.Exercise;
import io.javalin.http.Context;

import java.util.List;

public class ExerciseController implements IController {

    private final ExerciseDAO exerciseDAO;

    public ExerciseController(ExerciseDAO exerciseDAO) {
        this.exerciseDAO = exerciseDAO;
    }

    @Override
    public void getById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Exercise exercise = exerciseDAO.getById(id);
        if (exercise == null) {
            ctx.status(404).result("Exercise not found");
        } else {
            ctx.json(exercise);
        }
    }

    @Override
    public void create(Context ctx) {
        Exercise exercise = ctx.bodyAsClass(Exercise.class);
        Exercise createdExercise = exerciseDAO.create(exercise);
        ctx.status(201).json(createdExercise);
    }

    @Override
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Exercise updatedExercise = ctx.bodyAsClass(Exercise.class);
        exerciseDAO.update(id, updatedExercise);
        ctx.status(204); // No content
    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        exerciseDAO.delete(id);
        ctx.status(204); // No content
    }

    @Override
    public void getAll(Context ctx) {
        List<Exercise> exercises = exerciseDAO.getAll();
        ctx.json(exercises);
    }
}
