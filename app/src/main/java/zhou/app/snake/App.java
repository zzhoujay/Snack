package zhou.app.snake;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by zhou on 16-1-4.
 */
public class App extends Application {

    private Bus bus;

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        bus = new Bus();
    }

    public static App getApp() {
        return app;
    }

    public Bus getBus() {
        return bus;
    }
}
