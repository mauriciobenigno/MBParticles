package com.particles.android.programs;

import android.content.Context;


import com.particles.android.util.ShaderHelper;
import com.particles.android.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIZ = "u_Matrix";
    protected static final String U_UNIDADE_TEXTURA = "u_TextureUnit";
    protected static final String U_COR = "u_Color";
    // Attributs constants
    protected static final String A_POSICAO = "a_Position";
    protected static final String A_COR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String U_TEMPO = "u_Time";
    protected static final String A_VETOR_DE_DIRECAO = "a_DirectionVector";
    protected static final String A_PARTICULA_TEMPOINICIAL = "a_ParticleStartTime";
    protected static final String PARTICULA_EXTRA_VEC3 = "p_Extra";
    // Programa shader
    protected final int programa;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        // Compila os shaders e linka ao programa
        programa = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }
    public void useProgram() {
        // Seta o shader atual ao programa
        glUseProgram(programa);
    }
}