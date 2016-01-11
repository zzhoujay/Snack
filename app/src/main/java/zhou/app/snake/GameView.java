package zhou.app.snake;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import rx.functions.Action0;
import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-4.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;

    private GameMap gameMap;

    public GameView(Context context) {
        this(context, null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        int colNum = 40;
        int blockSize = getWidth() / colNum;
        int rowNum = getHeight() / blockSize;

        gameMap = new GameMap(colNum, rowNum, blockSize);

        Snack snack = new Snack(KeyMap.up.direction, blockSize, Color.RED);

        gameMap.setSnack(snack);

        drawThread = new DrawThread(holder, 30, Color.WHITE, true);

        drawThread.addDrawTask(gameMap, 0);

        drawThread.addActionTask(snack, 1, 100);

        startGameThread();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int colNum = 40;
        int blockSize = width / colNum;
        int rowNum = height / blockSize;

        gameMap.resetSize(colNum, rowNum, blockSize);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopGameThread();
    }

    private Thread startGameThread() {
        Thread thread = new Thread(drawThread);
        thread.start();
        return thread;
    }

    private void stopGameThread() {
        drawThread.finish();
    }


}
