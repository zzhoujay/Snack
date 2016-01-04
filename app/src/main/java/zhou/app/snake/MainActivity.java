package zhou.app.snake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    private int lastX = 0;
    private int lastY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int dx = x - lastX;
            int dy = y - lastY;

            int absX = Math.abs(dx);
            int absY = Math.abs(dy);
            if (absX > absY) {
                if (absX > 50) {
                    if (dx > 0) {
                        App.getApp().getBus().post(KeyMap.right);
                    } else {
                        App.getApp().getBus().post(KeyMap.left);
                    }
                }
            } else {
                if (absY > 50) {
                    if (dy > 0) {
                        App.getApp().getBus().post(KeyMap.down);
                    } else {
                        App.getApp().getBus().post(KeyMap.up);
                    }
                }
            }

            lastX = x;
            lastY = y;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
