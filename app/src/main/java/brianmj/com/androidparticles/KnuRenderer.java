package brianmj.com.androidparticles;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES32.*;

public class KnuRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "KNU_RENDERER";
    Context context;
    BlockingQueue<MotionEvent> inputEventQueue;
    float[] clearColorBuffer = new float[4];
    float[] clearDepthBuffer = new float[1];
    int viewWidth = 0;
    int viewHieght = 0;

    public KnuRenderer(Context context,
                       BlockingQueue<MotionEvent> inputEventQueue) {
        this.context = context;
        this.inputEventQueue = inputEventQueue;

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        clearColorBuffer[0] = 0.607843f; clearColorBuffer[1] = 0.803921f; clearColorBuffer[2] = 0.992156f; clearColorBuffer[3] = 1.0f;
        clearDepthBuffer[0] = 1.0f;

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        viewWidth = width;
        viewHieght = height;

        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glClearBufferfv(GL_DEPTH, 0, clearDepthBuffer, 0);
        glClearBufferfv(GL_COLOR, 0, clearColorBuffer, 0);

    }


}
