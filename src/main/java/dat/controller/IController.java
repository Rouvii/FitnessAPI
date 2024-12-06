package dat.controller;

import io.javalin.http.Context;

public interface IController {
    public void getById(Context ctx);
    public void create(Context ctx);
    public void update(Context ctx);
    public void delete(Context ctx);
    public void getAll(Context ctx);

}
