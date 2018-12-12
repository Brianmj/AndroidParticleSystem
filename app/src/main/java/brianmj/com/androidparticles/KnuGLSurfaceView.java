package brianmj.com.androidparticles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.ArrayBlockingQueue;

import javax.microedition.khronos.opengles.GL;

public class KnuGLSurfaceView extends GLSurfaceView {
    private static final String TAG = "KnuGLSurfaceView";
    private ArrayBlockingQueue<MotionEvent> inputEventQueue;
    private GLSurfaceView.Renderer renderer;

    private final int ARRAY_INPUT_QUEUE_SIZE = 5;

    // this constructor is used if you manually new up a GLView
    public KnuGLSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(3);
        int redBits = 8;
        int greenBits = 8;
        int blueBits = 8;
        int alphaBits = 8;
        int depthBits = 16;
        int stencilBits = 0;
        setEGLConfigChooser(redBits, greenBits, blueBits, alphaBits, depthBits, stencilBits);

        inputEventQueue = new ArrayBlockingQueue<>(ARRAY_INPUT_QUEUE_SIZE);

        renderer = new KnuRendererTextureAndShaderTest(this
                .getContext()
                .getApplicationContext(), inputEventQueue);
        // seteglcontextclientversion and configchooser need to be called before setRenderer
        setRenderer(renderer);
    }

    // use this constructor if just want to use the view from a layout
    public KnuGLSurfaceView(Context context, AttributeSet attribs){
        super(context, attribs);

        setEGLContextClientVersion(3);
        int redBits = 8;
        int greenBits = 8;
        int blueBits = 8;
        int alphaBits = 8;
        int depthBits = 16;
        int stencilBits = 0;
        setEGLConfigChooser(redBits, greenBits, blueBits, alphaBits, depthBits, stencilBits);

        inputEventQueue = new ArrayBlockingQueue<>(ARRAY_INPUT_QUEUE_SIZE);

        renderer = new KnuRendererTextureAndShaderTest(this.getContext().getApplicationContext(),
                inputEventQueue);
        // seteglcontextclientversion and configchooser need to be called before setRenderer
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        MotionEvent event1 = MotionEvent.obtain(event);

        Log.d(TAG, Thread.currentThread().getName());

        try{
            inputEventQueue.put(event1);
        }catch (InterruptedException except) {
            except.printStackTrace();
        }catch (NullPointerException except) {
            // why null pointer here?
            except.printStackTrace();
        }

        return true;
    }
}
