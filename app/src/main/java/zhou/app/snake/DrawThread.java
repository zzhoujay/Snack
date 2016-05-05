package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Action0;
import zhou.app.snake.interfaces.Callable;
import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-6.
 */

public final class DrawThread implements Runnable {


    private final int interval_time;
    private final SurfaceHolder holder;
    private final int refreshColor;
    private final boolean showFps;

    private final List<DrawTask> drawTasks;
    private final List<ActionTask> actionTasks;
    private Rect fpsRect;
    private boolean running;
    private Paint fpsPaint;
    private long lastTime;


    public DrawThread(SurfaceHolder holder, int fps, int refreshColor, boolean showFps) {
        this.holder = holder;
        drawTasks = new ArrayList<>();
        actionTasks = new ArrayList<>();
        running = true;
        interval_time = 1000 / fps;
        this.refreshColor = refreshColor;
        this.showFps = showFps;
        Rect rect = holder.getSurfaceFrame();
        fpsRect = new Rect(rect.right - 300, 0, rect.right, 150);
        fpsPaint = new Paint();
        fpsPaint.setColor(Color.BLACK);
        fpsPaint.setTextSize(40);
    }

    public DrawThread(SurfaceHolder holder, int fps) {
        this(holder, fps, Color.WHITE, false);
    }

    public DrawThread(SurfaceHolder holder) {
        this(holder, 30, Color.WHITE, false);
    }

    public DrawThread(SurfaceHolder holder, int fps, int refreshColor) {
        this(holder, fps, refreshColor, false);
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();

            // Call ActionTasks
            Stream.of(actionTasks).forEach(ActionTask::call);

            // Call DrawTasks
            synchronized (holder) {
                synchronized (drawTasks) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(refreshColor);
                        for (DrawTask drawTask : drawTasks) {
                            if (!running) {
                                return;
                            }
                            drawTask.draw(canvas);
                        }
                        // show fps
                        if (showFps)
                            drawFps(canvas);

                        holder.unlockCanvasAndPost(canvas);

                    }
                }
            }

            long endTime = System.currentTimeMillis();

            int remainTime = (int) (interval_time - (endTime - startTime));

            if (remainTime > 0) {
                try {
                    Thread.sleep(remainTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    private void drawFps(Canvas canvas) {
        long currTime = System.currentTimeMillis();
        if (lastTime != 0) {
            int fps = (int) (1000 / (currTime - lastTime));
            canvas.drawText(String.format("fps:%d", fps), fpsRect.centerX(), fpsRect.centerY(), fpsPaint);
        }
        lastTime = currTime;
    }

    public void finish() {
        running = false;
    }

    public void addDrawTask(Drawable task, int priority, int intervalTime) {
        drawTasks.add(new DrawTask(task, priority, intervalTime));
        Collections.sort(drawTasks);
    }

    public void addDrawTask(Drawable task, int priority) {
        drawTasks.add(new DrawTask(task, priority));
        Collections.sort(drawTasks);

    }

    public void addDrawTask(Drawable task) {
        drawTasks.add(new DrawTask(task));
        Collections.sort(drawTasks);

    }

    public void addDrawTask(DrawTask drawTask) {
        drawTasks.add(drawTask);
        Collections.sort(drawTasks);
    }

    public void removeTask(Drawable task) {
        for (DrawTask t : drawTasks) {
            if (t.action.equals(task)) {
                drawTasks.remove(t);
                return;
            }
        }
    }

    public void removeTask(DrawTask drawTask) {
        drawTasks.remove(drawTask);
    }

    public void addActionTask(ActionTask task) {
        actionTasks.add(task);
        Collections.sort(actionTasks);
    }

    public void addActionTask(Callable task, int priority, int intervalTime) {
        addActionTask(new ActionTask(task, priority, intervalTime));
    }

    public void addActionTask(Callable task, int priority) {
        addActionTask(new ActionTask(task, priority));
    }

    public void addActionTask(Callable task) {
        addActionTask(new ActionTask(task));
    }

    public void removeTask(Action0 task) {
        for (ActionTask actionTask : actionTasks) {
            if (actionTask.action.equals(task)) {
                actionTasks.remove(actionTask);
                return;
            }
        }
    }

    public void removeTask(ActionTask task) {
        actionTasks.remove(task);
    }

    private static class DrawTask implements Comparable<DrawTask>, Drawable {

        public Drawable action;
        public int priority;
        public int intervalTime;

        private long lastCallTime;

        public DrawTask(Drawable action, int priority, int intervalTime) {
            this.action = action;
            this.priority = priority;
            this.intervalTime = intervalTime;
        }

        public DrawTask(Drawable action, int priority) {
            this.action = action;
            this.priority = priority;
            intervalTime = -1;
        }

        public DrawTask(Drawable action) {
            this.action = action;
            priority = 0;
            intervalTime = -1;
        }

        @Override
        public void draw(Canvas canvas) {
            if (intervalTime <= 0) {
                action.draw(canvas);
                return;
            }
            long currTime = System.currentTimeMillis();
            if (currTime - lastCallTime >= intervalTime) {
                action.draw(canvas);
                lastCallTime = currTime;
            }
        }

        @Override
        public int compareTo(@NonNull DrawTask another) {
            return priority - another.priority;
        }


        @Override
        public int hashCode() {
            int result = action.hashCode();
            result = 31 * result + priority;
            return result;
        }
    }

    private static class ActionTask implements Callable, Comparable<ActionTask> {

        private Callable action;
        private int priority;
        private int intervalTime;

        private long lastCallTime;

        public ActionTask(Callable action, int priority, int intervalTime) {
            this.action = action;
            this.priority = priority;
            this.intervalTime = intervalTime;
        }

        public ActionTask(Callable action, int priority) {
            this(action, priority, -1);
        }

        public ActionTask(Callable action) {
            this(action, 0, -1);
        }

        @Override
        public void call() {
            if (intervalTime <= 0) {
                action.call();
            } else {
                long currTime = System.currentTimeMillis();
                if (currTime - lastCallTime >= intervalTime) {
                    action.call();
                    lastCallTime = currTime;
                }
            }

        }

        @Override
        public int compareTo(@NonNull ActionTask another) {
            return priority - another.priority;
        }
    }
}
