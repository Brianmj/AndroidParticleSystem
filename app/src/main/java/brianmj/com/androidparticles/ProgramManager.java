package brianmj.com.androidparticles;

import android.content.Context;
import android.graphics.Shader;
import android.opengl.GLES32;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static android.opengl.GLES32.*;

public class ProgramManager {
    private Map<UUID, ProgramObject> programManager;  // the UUID will be used as the key
    // the integer to hold the actual program object


    public ProgramManager() {
        programManager = new HashMap<>();
    }

    UUID buildGraphicsProgramRaw(Context context, int rawVertexFileId, int rawFragmentFileId)
    {
        ProgramObject programObject = doBuildGraphicsProgramFromRawResource(context, rawVertexFileId, rawFragmentFileId);
        // we have a valid programId.
        UUID uuid = addProgramToManager(programObject);

        return uuid;
    }

    UUID buildGraphicsProgramAssets(Context context, String vertexFile, String fragmentFile)
    {
        ProgramObject programObject = doBuildGraphicsProgramFromAssets(context, vertexFile, fragmentFile);
        // we have a valid programId.
        UUID uuid = addProgramToManager(programObject);

        return uuid;
    }

    public void activateProgram(UUID uuid)
    {
        Optional<ProgramObject> programId = getProgram(uuid);

        if(programId.isPresent())
            glUseProgram(programId.get().getProgramObject());
    }

    public void disableProgram() {
        glUseProgram(0);
    }

    private ProgramObject doBuildGraphicsProgramFromRawResource(Context context, int vertexFileId, int fragmentFileId) {
        ShaderObject vertexShader = buildVertex(context, vertexFileId);
        ShaderObject fragmentShader = buildFragment(context, fragmentFileId);

        ProgramObject programObject = new ProgramObject(glCreateProgram());
        attachShaderToProgram(programObject, vertexShader);
        attachShaderToProgram(programObject, fragmentShader);

        linkProgram(programObject);
        checkProgramStatus(programObject);

        detachAndDeleteShader(programObject, vertexShader);
        detachAndDeleteShader(programObject, fragmentShader);

        return programObject;
    }

    private ProgramObject doBuildGraphicsProgramFromAssets(Context context, String vertexFile, String fragmentFile) {
        ShaderObject vertexShader = buildVertex(context, vertexFile);
        ShaderObject fragmentShader = buildFragment(context, fragmentFile);

        ProgramObject programObject = new ProgramObject(glCreateProgram());
        attachShaderToProgram(programObject, vertexShader);
        attachShaderToProgram(programObject, fragmentShader);

        linkProgram(programObject);
        checkProgramStatus(programObject);

        detachAndDeleteShader(programObject, vertexShader);
        detachAndDeleteShader(programObject, fragmentShader);

        return programObject;
    }


    private InputStream openStreamToFile(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);

        return inputStream;
    }

    private InputStream openStreamToFile(Context context, int rawResourceId) throws IOException {
        InputStream inputStream = context.getResources().openRawResource(rawResourceId);

        return inputStream;
    }

    private String readSource(InputStream inputStream) throws IOException {
        /*val size = channel.size()
        val buffer = ByteBuffer.allocate(size.toInt())
        val bytesRead = channel.read(buffer)
        if(bytesRead != size.toInt())
            throw Exception("Not all bytes could be read. Bytes read: $bytesRead. size of file: $size")
        val source = String(buffer.array())
        channel.close()*/


        StringBuilder sb = new StringBuilder();
        try (BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {

            String line = buf.readLine();

            while (line != null) {
                sb.append(line);
                line = buf.readLine();
            }
        }


        return sb.toString();
    }

    private ShaderObject buildVertex(Context context, String vertexFile) {
        ShaderObject shaderObject = null;
        try (InputStream vertexStream = openStreamToFile(context, vertexFile)) {
            String vertexSource = readSource(vertexStream);
            shaderObject = buildShader(vertexSource, ShaderType.Vertex);
            compileShader(shaderObject);
            checkShaderStatus(shaderObject);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }

        return shaderObject;
    }

    private ShaderObject buildVertex(Context context, int vertexRawResourceId) {
        ShaderObject shaderObject = null;
        try (InputStream vertexStream = openStreamToFile(context, vertexRawResourceId)) {
            String vertexSource = readSource(vertexStream);
            shaderObject = buildShader(vertexSource, ShaderType.Vertex);
            compileShader(shaderObject);
            checkShaderStatus(shaderObject);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }

        return shaderObject;
    }

    private ShaderObject buildFragment(Context context, String fragmentFile) {
        ShaderObject shaderObject = null;
        try (InputStream fragmentStream = openStreamToFile(context, fragmentFile)) {
            String fragmentSource = readSource(fragmentStream);
            shaderObject = buildShader(fragmentSource, ShaderType.Fragment);
            compileShader(shaderObject);
            checkShaderStatus(shaderObject);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }

        return shaderObject;
    }

    private ShaderObject buildFragment(Context context, int rawResourceFragmentId) {
        ShaderObject shaderObject = null;
        try (InputStream fragmentStream = openStreamToFile(context, rawResourceFragmentId)) {
            String fragmentSource = readSource(fragmentStream);
            shaderObject = buildShader(fragmentSource, ShaderType.Fragment);
            compileShader(shaderObject);
            checkShaderStatus(shaderObject);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }

        return shaderObject;
    }

    private ShaderObject buildCompute(Context context, String computeFile) {
        ShaderObject shaderObject = null;
        try (InputStream computeStream = openStreamToFile(context, computeFile)) {
            String computeSource = readSource(computeStream);
            shaderObject = buildShader(computeSource, ShaderType.Compute);
            compileShader(shaderObject);
            checkShaderStatus(shaderObject);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }

        return shaderObject;
    }

    private ShaderObject buildCompute(Context context, int rawResourceComputeId) {
        ShaderObject shaderObject = null;
        try (InputStream computeStream = openStreamToFile(context, rawResourceComputeId)) {
            String computeSource = readSource(computeStream);
            shaderObject = buildShader(computeSource, ShaderType.Compute);
            compileShader(shaderObject);
            checkShaderStatus(shaderObject);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }

        return shaderObject;
    }

    private ShaderObject buildShader(String shaderSource, ShaderType st) {

        int shader = 0;

        switch (st) {
            case Vertex: {
                shader = glCreateShader(GLES32.GL_VERTEX_SHADER);
            }
            break;

            case Fragment: {
                shader = glCreateShader(GL_FRAGMENT_SHADER);
            }
            break;

            case Compute: {
                shader = glCreateShader(GL_COMPUTE_SHADER);
            }
            break;
        }

        // attach the source to the shader
        glShaderSource(shader, shaderSource);

        return new ShaderObject(shader, st);

    }

    private void compileShader(ShaderObject shaderObject) {
        glCompileShader(shaderObject.getShaderObject());
    }

    private void linkProgram(ProgramObject po) {
        glLinkProgram(po.getProgramObject());
    }

    private void checkShaderStatus(ShaderObject so) {
        int[] success = {0};
        glGetShaderiv(so.getShaderObject(), GL_COMPILE_STATUS, success, 0);

        if (success[0] <= GL_FALSE) {
            // failure
            String errorString = glGetShaderInfoLog(so.getShaderObject());
            String resultString = so.getShaderType().name() + ": " + errorString;
            throw new RuntimeException(resultString);
        }
    }

    private void checkProgramStatus(ProgramObject po) {
        int[] success = {0};
        glGetProgramiv(po.getProgramObject(), GL_LINK_STATUS, success, 0);

        if (success[0] <= GL_FALSE) {
            String tempString = glGetProgramInfoLog(po.getProgramObject());
            String errorString = "Program error: " + tempString;

            throw new RuntimeException(errorString);
        }

        glValidateProgram(po.getProgramObject());
        int[] validationStatus = {0};
        glGetProgramiv(po.getProgramObject(), GL_VALIDATE_STATUS, validationStatus, 0);

        if (validationStatus[0] <= GL_FALSE) {
            String tempString = glGetProgramInfoLog(po.getProgramObject());
            String errorString = "Validation error: " + tempString;

            throw new RuntimeException(errorString);
        }
    }

    private void attachShaderToProgram(ProgramObject po, ShaderObject so) {
        glAttachShader(po.getProgramObject(), so.getShaderObject());
    }

    // this code should be converted to java if needed
    private void detachAndDeleteShader(ProgramObject programId, ShaderObject shaderId) {
        glDetachShader(programId.getProgramObject(), shaderId.getShaderObject());
        glDeleteShader(shaderId.getShaderObject());
    }

    private UUID addProgramToManager(ProgramObject po) {
        UUID uuid = UUID.randomUUID();
        programManager.put(uuid, po);

        return uuid;
    }

    private Optional<ProgramObject> removeProgramFromManager(UUID uuid) {
        return Optional.ofNullable(programManager.remove(uuid));
    }

    private Optional<ProgramObject> getProgram(UUID uuid) {
        return Optional.ofNullable(programManager.get(uuid));
    }

    private enum ShaderType {
        Vertex, Fragment, Compute, Tessalation;
    }

    // a simple class to hold an opengl program object
    private static class ProgramObject {
        private int programObject;

        public ProgramObject(int programObject) {
            this.programObject = programObject;
        }

        int getProgramObject() {
            return programObject;
        }
    }

    // a simple class to represent an opengl shader object
    private static class ShaderObject {
        private int shaderObject;
        private ShaderType shaderType;

        public ShaderObject(int shaderObject, ShaderType shaderType) {
            this.shaderObject = shaderObject;
            this.shaderType = shaderType;
        }

        int getShaderObject() {
            return shaderObject;
        }

        ShaderType getShaderType() {
            return shaderType;
        }
    }
}
