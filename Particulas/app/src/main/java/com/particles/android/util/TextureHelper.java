package com.particles.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper {
    private static final String TAG = "TextureHelper";
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Não foi possível gerar um novo objeto de textura OpenGL.");
            }
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
            }
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        // Configura o OpenGL para configurar a textura como 3D, indica o ID de textura.
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        // Suavização de texturas usando mipMaps e filtragens lineares ou trilineares
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); // Minimização com filtragem trilinear
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); // maximização com filtragem linear
        // carregamento de textura
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        // Limpar a memoria imediatamento após o carregamento ao OpenGL
        bitmap.recycle(); // Assim evita-se lixo ou um trabalho extra ao Dalvik cache
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture (GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }
}
