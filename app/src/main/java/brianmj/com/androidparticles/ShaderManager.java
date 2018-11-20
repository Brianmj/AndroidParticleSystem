package brianmj.com.androidparticles;

import android.opengl.GLES32;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static android.opengl.GLES32.*;

public class ShaderManager {
    private Map<UUID, ProgramObject> programManager;  // the UUID will be used as the key
                                                // the integer to hold the actual program object


    public ShaderManager() {
        programManager = new HashMap<>();
    }

    void attachShaderToProgram(ProgramObject po, ShaderObject so) {
        glAttachShader(po.getProgramObject(), so.getShaderObject());
    }

    // this code should be converted to java if needed
    /*private fun detachAndDeleteShader(programId: Int, shaderId: Int)
    {
        GLES20.glDetachShader(programId, shaderId)
        GLES20.glDeleteShader(shaderId)
    }*/

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

    private enum ProgramType {
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

        public ShaderObject(int shaderObject) {
            this.shaderObject = shaderObject;
        }

        int getShaderObject() {
            return shaderObject;
        }
    }
}
