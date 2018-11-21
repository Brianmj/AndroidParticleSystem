package brianmj.com.androidparticles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class KnuGLSurfaceView extends GLSurfaceView {

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
        setRenderer(new KnuRenderer(this.getContext().getApplicationContext()));
    }

    // use this construtor if just want to use the view from a layout
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
        setRenderer(new KnuRenderer(this.getContext().getApplicationContext()));
    }
}
