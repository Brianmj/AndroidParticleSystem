package brianmj.com.androidparticles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.View;

import javax.microedition.khronos.opengles.GL;

public class KnuGLSurfaceView extends GLSurfaceView implements View.OnTouchListener {

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

        // seteglcontextclientversion and configchooser need to be called before setRenderer
        setRenderer(new KnuRenderer(this.getContext().getApplicationContext()));
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

        // seteglcontextclientversion and configchooser need to be called before setRenderer
        setRenderer(new KnuRendererTextureAndShaderTest(this.getContext().getApplicationContext()));
    }
}
