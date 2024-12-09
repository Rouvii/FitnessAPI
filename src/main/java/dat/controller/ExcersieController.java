package dat.controller;

import dat.dao.ExcersieDAO;
import dat.dto.ExerciseDTO;
import dat.exception.ApiException;
import dat.exception.Message;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcersieController implements IController {
    private final ExcersieDAO excersieDAO;
    private final Logger log = LoggerFactory.getLogger(ExcersieController.class);

    public ExcersieController(ExcersieDAO excersieDAO) {
        this.excersieDAO = excersieDAO;
    }

    @Override
    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ExerciseDTO exercise = excersieDAO.getById(id);
            if (exercise == null) {
                ctx.status(404);
                ctx.json(new Message(404, "Exercise not found"));
                return;
            }
            ctx.status(200);
            ctx.json(exercise);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid ID format");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new ApiException(500, "An unexpected error occurred");
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            ExerciseDTO exerciseDTO = ctx.bodyAsClass(ExerciseDTO.class);
            ExerciseDTO createdExercise = excersieDAO.create(exerciseDTO);
            ctx.status(201);
            ctx.json(createdExercise);
        } catch (Exception e) {
            log.error("Error creating exercise: {}", e.getMessage());
            throw new ApiException(400, "Invalid input");
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ExerciseDTO updatedExerciseData = ctx.bodyAsClass(ExerciseDTO.class);

            // Call the DAO update method to perform the update operation
            excersieDAO.update(id, updatedExerciseData);

            // Check if the exercise was updated (you can implement additional logic if needed)
            ExerciseDTO updatedExercise = excersieDAO.getById(id); // Retrieve the updated exercise
            if (updatedExercise == null) {
                ctx.status(404);
                ctx.json(new Message(404, "Exercise not found"));
                return;
            }

            ctx.status(200);
            ctx.json(updatedExercise);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid ID format");
        } catch (Exception e) {
            log.error("Error updating exercise: {}", e.getMessage());
            throw new ApiException(400, "Invalid input");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            // Parse the 'id' from the URL path parameter
            int id = Integer.parseInt(ctx.pathParam("id"));

            // Call the delete method in DAO. Assuming delete doesn't return anything.
            excersieDAO.delete(id); // If the deletion fails, an exception will be thrown.

            // If deletion is successful, return a 204 (No Content) status
            ctx.status(204);
        } catch (NumberFormatException e) {
            // Log and return a 400 status if the ID is invalid
            log.error("Invalid ID format: {}", e.getMessage());
            ctx.status(400);
            ctx.json(new Message(400, "Invalid ID format"));
        } catch (IllegalArgumentException e) {
            // Handle the case where the exercise is not found
            log.error("Exercise not found: {}", e.getMessage());
            ctx.status(404);
            ctx.json(new Message(404, "Exercise not found"));
        } catch (Exception e) {
            // Catch all other exceptions
            log.error("Error deleting exercise: {}", e.getMessage());
            ctx.status(500);
            ctx.json(new Message(500, "An unexpected error occurred"));
        }
    }

    @Override
    public void getAll(Context ctx) {
        try {
            ctx.status(200);
            ctx.json(excersieDAO.getAll());
        } catch (Exception e) {
            log.error("Error fetching exercises: {}", e.getMessage());
            throw new ApiException(500, "An unexpected error occurred");
        }
    }
}