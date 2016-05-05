package zhou.app.snake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    private int lastX = 0;
    private int lastY = 0;
    private long lastTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                lastTime = System.currentTimeMillis();
                return true;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int time = (int) (System.currentTimeMillis() - lastTime);
                float vx = (x - lastX) / (float) time;
                float vy = (y - lastY) / (float) time;
                handleTouchEvent(vx, vy);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void handleTouchEvent(float vx, float vy) {
        float absVx = Math.abs(vx);
        float absVy = Math.abs(vy);
        if (absVx > absVy) {
            KeyMap keyMap;
            if (vx > 0) {
                keyMap = KeyMap.right;
            } else {
                keyMap = KeyMap.left;
            }
            App.getApp().getBus().post(keyMap);
        } else {
            KeyMap keyMap;
            if (vy > 0) {
                keyMap = KeyMap.down;
            } else {
                keyMap = KeyMap.up;
            }
            App.getApp().getBus().post(keyMap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
