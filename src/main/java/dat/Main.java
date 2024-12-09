package dat;

import dat.config.ApplicationConfig;
import dat.config.Populate;

public class Main {

    public static void main(String[] args) {
        Populate.populate();
        ApplicationConfig.startServer(7070);
    }
}