package dat.dao;

import java.util.List;

public interface IDao<T> {
    public List<T> getAll();
    public T getById(long id);
    public void create(T t);
    public void update(T t, T update);
    public void delete(long id);
}
