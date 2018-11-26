package brianmj.com.androidparticles;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.UUID;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.glGetIntegerv;
import static android.opengl.GLES30.GL_MAJOR_VERSION;
import static android.opengl.GLES30.GL_MINOR_VERSION;
import static android.opengl.GLES32.*;
import static android.opengl.GLES32.GL_COLOR;
import static android.opengl.GLES32.GL_DEPTH;
import static android.opengl.GLES32.glClearBufferfv;
import static android.opengl.GLES32.glViewport;

public class KnuRendererTextureAndShaderTest implements GLSurfaceView.Renderer, DebugProc {

    private static final String TAG = "KNU_RENDERER_TEXTURE_AND_SHADER_TEST";
    Context context;
    float[] clearColorBuffer = new float[4];
    float[] clearDepthBuffer = new float[1];
    int viewWidth = 0;
    int viewHieght = 0;

    TextureManager textureManager = new TextureManager();
    ProgramManager programManager = new ProgramManager();

    int[] vbo = {0};
    int[] vao = {0};

    float[] projectionMatrix = new float[16];
    float[] modelviewMatrix = new float[16];

    int graphicsError = 0;

    UUID textureID = null;
    UUID programID = null;

    public KnuRendererTextureAndShaderTest(Context context) {
        this.context = context;

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        graphicsError = glGetError();
        clearColorBuffer[0] = 0.607843f; clearColorBuffer[1] = 0.803921f; clearColorBuffer[2] = 0.992156f; clearColorBuffer[3] = 1.0f;
        clearDepthBuffer[0] = 1.0f;

        int[] majorVersion = {0};
        int[] minorVersion = {0};
        glGetIntegerv(GL_MAJOR_VERSION, majorVersion, 0);
        glGetIntegerv(GL_MINOR_VERSION, minorVersion, 0);

        graphicsError = glGetError();
        String textureImage = "images/paladin.png";
        textureID = textureManager.load2DTextureFromAssets(context, textureImage);

        graphicsError = glGetError();

        String vertexShader = "shaders/quad_texture.vert";
        String fragmentShader = "shaders/quad_texture.frag";
        programID = programManager.buildGraphicsProgramAssets(context, vertexShader, fragmentShader);

        graphicsError = glGetError();
        glGenBuffers(1, vbo, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
                                    //vertex                    texcoords
        float[] vertexBuffer = {-4.0f, 4.0f, 0.0f, 1.0f,        0f, 1f,
                                -4.0f, -4.0f, 0.0f, 1.0f,       0f, 0f,
                                4.0f,   4.0f, 0.0f, 1.0f,       1f, 1f,
                                4.0f,   4.0f, 0.0f, 1.0f,       1f, 1f,
                                -4.0f, -4.0f, 0.0f, 1.0f,       0f, 0f,
                                4.0f,   -4.0f, 0.0f, 1.0f,      1f, 0f};

        final int SIZE_OF_FLOAT_BYTES = 4;
        int bufferSize = vertexBuffer.length * SIZE_OF_FLOAT_BYTES;
        Buffer buffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(vertexBuffer);

        buffer.flip();
        glBufferData(GL_ARRAY_BUFFER, bufferSize, buffer, GL_STATIC_DRAW);

        glGenVertexArrays(1, vao, 0);
        glBindVertexArray(vao[0]);

        // specify the vertex attrib
        glVertexAttribPointer(0, 4, GL_FLOAT, false, SIZE_OF_FLOAT_BYTES * 6, 0);
        glEnableVertexAttribArray(0);

        // specify the texture coord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, SIZE_OF_FLOAT_BYTES * 6, SIZE_OF_FLOAT_BYTES * 4);
        glEnableVertexAttribArray(1);

        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.frustumM(projectionMatrix, 0, -3.0f, 3.0f, -3.0f, 3.0f,
                0.1f, 100.0f);

        Matrix.setIdentityM(modelviewMatrix, 0);
        Matrix.translateM(modelviewMatrix, 0, 0.0f, 0.0f, -0.2f);
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

        glActiveTexture(GL_TEXTURE0 + 1);
        programManager.activateProgram(programID);
        textureManager.bindTexture(textureID);

        glUniformMatrix4fv(0, 1, false, projectionMatrix, 0);
        glUniformMatrix4fv(1, 1, false, modelviewMatrix, 0);

        glBindVertexArray(vao[0]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

    }

    @Override
    public void onMessage(int source, int type, int id, int severity, String message) {
        String error = "Source: " + source + ", Type: " + type + ", ID: " + id
                + ", Severity: " + severity + ", Message: " + message;

        Log.d(TAG, error);
    }
}
