package dat.dao;

import java.util.List;

public interface IDao<T> {
    public List<T> getAll();
    public T getById(int id);
    public T create(T t);
    public void update(T t, T update);
    public void delete(int id);
}
