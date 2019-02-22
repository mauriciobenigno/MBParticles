package com.particles.android.programs;

import android.content.Context;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ParticleShaderProgram extends ShaderProgram{
    // Localização das variaveis Uniform no shader
    private final int uLocalMatriz;
    private final int uLocalTempo;
    // Localização das variaveis Atribute no shader
    private final int aLocalPosicao;
    private final int aLocalCor;
    private final int aLocalVetorDirecao;
    private final int aLocalTempoInicialParticula;
    private final int pExtra;
    private final int uTextureUnitLocation;

    public ParticleShaderProgram(Context context, int vertex_shader, int fragment_shader) {
        super(context,vertex_shader,fragment_shader);
        uLocalMatriz = glGetUniformLocation(programa, U_MATRIZ);
        uLocalTempo = glGetUniformLocation(programa, U_TEMPO);
        aLocalPosicao = glGetAttribLocation(programa, A_POSICAO);
        aLocalCor = glGetAttribLocation(programa, A_COR);
        aLocalVetorDirecao = glGetAttribLocation(programa, A_VETOR_DE_DIRECAO);
        aLocalTempoInicialParticula = glGetAttribLocation(programa, A_PARTICULA_TEMPOINICIAL);
        pExtra = glGetAttribLocation(programa, PARTICULA_EXTRA_VEC3);
        // Inicializando a textura
        uTextureUnitLocation = glGetUniformLocation(programa, U_UNIDADE_TEXTURA);
    }

    public void setUniforms(float[] matrix, float elapsedTime, int textureId) {
        glUniformMatrix4fv(uLocalMatriz, 1, false, matrix, 0);
        glUniform1f(uLocalTempo, elapsedTime);
        // -- [ APLICAÇÃO DE TEXTURA ] --
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getLocalPosicao()
    {
        return aLocalPosicao;
    }

    public int getLocalCor()
    {
        return aLocalCor;
    }

    public int getLocalVetorDeDirecao()
    {
        return aLocalVetorDirecao;
    }

    public int getLocalTempoInicialParticula()
    {
        return aLocalTempoInicialParticula;
    }

    public int getLocalInformacoesExtras() { return pExtra; }

    public int getProgramID() { return programa; }
}
