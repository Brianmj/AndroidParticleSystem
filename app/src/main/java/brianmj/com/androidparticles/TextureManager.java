package brianmj.com.androidparticles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES32.*;

public class TextureManager {

    private Map<UUID, TextureObject> textureMap = new HashMap<>();
    private TextureLoader loader = new TextureLoader();

    public UUID load2DTextureFromAssets(Context context, String file) {
        TextureObject textureObject = loader.loadTextureAssets(context, file);

        return addTextureToMap(textureObject);
    }

    public void bindTexture(UUID textureId) {

        TextureObject textureObject = textureMap.get(textureId);
        if (textureObject != null) {
            glBindTexture(textureObject.getTextureTarget(), textureObject.getTextureId());
        }
    }

    private UUID addTextureToMap(TextureObject textureObject) {
        UUID uuid = UUID.randomUUID();
        textureMap.put(uuid, textureObject);
        return uuid;
    }

    // private utility class for loading a texture
    private class TextureLoader {
        TextureObject loadTextureAssets(Context context, String textureName) {

            TextureObject texObj = null;

            try (InputStream is = openStreamToFile(context, textureName)) {
                Bitmap bitmap = decodeInputStream(is);
                texObj = build2DTexture(bitmap);

            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }

            return texObj;
        }

        private InputStream openStreamToFile(Context context, String fileName) throws IOException {
            return context.getAssets().open(fileName);
        }

        private Bitmap decodeInputStream(InputStream is) {
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            if (bitmap == null) {
                // error
                throw new RuntimeException("Was unable to decode bitmap");
            }

            return bitmap;
        }

        private TextureObject build2DTexture(Bitmap bitmap) {
            int textureWidth = bitmap.getWidth();
            int textureHeight = bitmap.getHeight();
            Bitmap.Config config = bitmap.getConfig();

            ByteBuffer pixelData = ByteBuffer.allocate(bitmap.getByteCount());
            bitmap.copyPixelsToBuffer(pixelData);
            pixelData.flip();

            return buildGL2DTexture(textureWidth, textureHeight, config, pixelData);

        }

        private TextureObject buildGL2DTexture(int textureWidth, int textureHeight, Bitmap.Config config, ByteBuffer pixelData) {
            int[] texObj = {0};
            glGenTextures(1, texObj, 0);
            glBindTexture(GL_TEXTURE_2D, texObj[0]);

            switch (config) {
                case ARGB_8888: {
                    glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, textureWidth, textureHeight);
                    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, textureWidth,
                            textureHeight, GL_RGBA, GL_BYTE, pixelData);

                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                }
                break;

                default: {
                    // not supporting other formats at this time
                    throw new RuntimeException("Unsupported format: " + config);
                }
            }

            return new TextureObject(texObj[0], GL_TEXTURE_2D, textureWidth, textureHeight);

        }

    }

    private class TextureObject {
        private int textureId;
        private int textureTarget;
        private int width;
        private int height;

        TextureObject(int textureId, int textureTarget, int width, int height) {
            this.textureId = textureId;
            this.textureTarget = textureTarget;
            this.width = width;
            this.height = height;
        }

        void dispose() {
            int[] textures = {textureId};
            glDeleteTextures(1, textures, 0);
            textureId = -1;
            textureTarget = -1;
        }

        public int getTextureId() {
            return textureId;
        }

        public int getTextureTarget() {
            return textureTarget;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

}

