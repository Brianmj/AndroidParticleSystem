package brianmj.com.androidparticles;

import android.opengl.GLES32;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import static android.opengl.GLES32.*;

interface EngineInterface{

    int getWindowWidth();
    int getWindowHeight();

}
public class Engine implements EngineInterface {

    private static final int MATRIX_ELEMENT_COUNT = 16;

    private int windowWidth;
    private int windowHeight;
    private float[] orthographicMatrix;
    private float[] perspectiveMatrix;

    Engine() {
        windowWidth = 0;
        windowHeight = 0;
        orthographicMatrix = new float[MATRIX_ELEMENT_COUNT];
        perspectiveMatrix = new float[MATRIX_ELEMENT_COUNT];
    }

    public void resize(int newWidth, int newHeight){
        glViewport(0, 0, newWidth, newHeight);

        int halfWidth = newWidth >> 1;
        int halfHeight = newHeight >> 1;

        

    }

    @Override
    public int getWindowWidth() {
        return windowWidth;
    }

    @Override
    public int getWindowHeight() {
        return windowHeight;
    }
}
