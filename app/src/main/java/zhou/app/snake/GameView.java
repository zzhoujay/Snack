package zhou.app.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhou on 16-1-4.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int interval_time = 100 / 3;

    private final SurfaceHolder holder;
    private boolean gameRunning;
    private Paint paint;
    private Snack snack;
    private int colNum, rowNum, blockSize;

    public GameView(Context context) {
        this(context, null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        colNum = 40;
        blockSize = getWidth() / colNum;
        rowNum = getHeight() / blockSize;

        snack = new Snack(KeyMap.up.direction, blockSize, Color.RED);

        gameRunning = true;
        startGameThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        App.getApp().getBus().unregister(snack);
        stopGameThread();
    }

    private long ggt;

    public void drawGame(Canvas canvas) {
        snack.draw(canvas);

        if (System.currentTimeMillis() - ggt > 100) {
            snack.next();
            ggt = System.currentTimeMillis();
        }
    }

    private Thread startGameThread() {
        Thread thread = new Thread(drawThread);
        thread.start();
        return thread;
    }

    private void stopGameThread() {
        gameRunning = false;
    }


    private final Runnable drawThread = new Runnable() {

        @Override
        public void run() {

            while (gameRunning) {
                long startTime = System.currentTimeMillis();

                synchronized (holder) {
                    Canvas canvas = holder.lockCanvas();
                    canvas.drawColor(Color.WHITE);
                    drawGame(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }

                long endTime = System.currentTimeMillis();

                while (endTime - startTime < interval_time) {
                    endTime = System.currentTimeMillis();
                    Thread.yield();
                }
            }


        }
    };
}
