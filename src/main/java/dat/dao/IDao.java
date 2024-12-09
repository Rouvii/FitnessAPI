package dat.dao;

import dat.dto.ExerciseDTO;

import java.util.List;

public interface IDao<T> {
    public List<T> getAll();
    public T getById(int id);
    public T create(T t);

   public void update(int id, T t);

    public void delete(int id);
}
