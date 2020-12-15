import Proxy.Proxy;

import java.io.IOException;
import java.util.HashMap;

public class App {
    public static void main(String[] args) throws IOException {
        ////Main.main(args);
        new Proxy(Integer.parseInt(args[0])).run();



    }
}
