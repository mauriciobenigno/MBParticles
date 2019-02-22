package com.particles.android.objects;

import android.graphics.Color;

import com.particles.android.data.VertexArray;
import com.particles.android.programs.ParticleShaderProgram;
import com.particles.android.util.Geometry;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.particles.android.Constants.BYTES_PER_FLOAT;

public class SistemaParticulas {
    // Variáveis para contade de posição de memoria dos componentes
    private static final int COMPONENTE_POSICAO = 3;
    private static final int COMPONENTE_COR = 3;
    private static final int COMPONENTE_VETOR = 3;
    private static final int COMPONENTE_TEMPO_INICIAL_PARTICULA = 1;
    private static final int PARTICULA_EXTRA_VEC3 = 3;
    private static final int CONTAGEM_TOTAL_COMPONENTES =
            COMPONENTE_POSICAO
                    + COMPONENTE_COR
                    + COMPONENTE_VETOR
                    + COMPONENTE_TEMPO_INICIAL_PARTICULA
                    + PARTICULA_EXTRA_VEC3;
    private static final int BYTESTOTAIS_STRIDE = CONTAGEM_TOTAL_COMPONENTES * BYTES_PER_FLOAT;
    private final float[] particulas;
    private final VertexArray vertexArray;
    private final int maximoParticulas;
    private int particulaAtual;
    private int proximaParticula;
    // -- [ INFOS EXTRAS ] --
    private Geometry.Point extra;

    public SistemaParticulas(int maximoDeParticulas) {
        /* Calcula o espaço de memoria  de uma particula em um vetor estatico
        * o calculo se dá pelo numero maximo de particulas que serão usadas
        * multiplicado pelo espaço que os atributos dessa particula irá ocupar*/
        particulas = new float[maximoDeParticulas * CONTAGEM_TOTAL_COMPONENTES];
        /* aloca no vetor o espaço necessário armanezamento das particulas */
        vertexArray = new VertexArray(particulas);
        /* Atribui a contagem maxima de particulas ao atributo do Sistema de particulas*/
        this.maximoParticulas = maximoDeParticulas;
        /* Atribui valores zerados para o Extra*/
        this.extra = new Geometry.Point(0.0f,0.0f,0.0f);
    }

    public void addParticula(Geometry.Point posicao, int cor, Geometry.Vector direcao, float tempoInicial) {
        /* As variaveis de deslocamento servem para calcular o deslocamento total de memoria entre os
         * componentes internos da particula adicionada */
        final int deslocamentoInicial = proximaParticula * CONTAGEM_TOTAL_COMPONENTES;
        int deslocamentoAtual = deslocamentoInicial;
        proximaParticula++; // Guarda o ID da proxima particula
        // Verifica se o numero de particulas já chegou ao maximo
        if (particulaAtual < maximoParticulas) {
            particulaAtual++;
        }
        if (proximaParticula == maximoParticulas) {
            // Volta a contagem pra zero, matando as particulas antigas e reiniciando o envio de particulas
            proximaParticula = 0;
        }
        /* DADOS DAS PARTICULAS */
        // -- [ POSIÇÃO ] --
        particulas[deslocamentoAtual++] = posicao.x;
        particulas[deslocamentoAtual++] = posicao.y;
        particulas[deslocamentoAtual++] = posicao.z;
        // -- [ COR ] --
        particulas[deslocamentoAtual++] = Color.red(cor) / 255f;
        particulas[deslocamentoAtual++] = Color.green(cor) / 255f;
        particulas[deslocamentoAtual++] = Color.blue(cor) / 255f;
        // -- [ DIRECAO ] --
        particulas[deslocamentoAtual++] = direcao.x;
        particulas[deslocamentoAtual++] = direcao.y;
        particulas[deslocamentoAtual++] = direcao.z;
        // -- [ INICIO DO TEMPO DE VIDA ] --
        particulas[deslocamentoAtual++] = tempoInicial;
        // -- [ TESTE DE INFORMAÇÕES EXTRAS ] --
        particulas[deslocamentoAtual++] = extra.x;
        particulas[deslocamentoAtual++] = extra.z;
        particulas[deslocamentoAtual++] = extra.y;
        // -- [ ENVIADO PARTICULA PARA O VETOR "memoria" ] --
        vertexArray.updateBuffer(particulas, deslocamentoInicial, CONTAGEM_TOTAL_COMPONENTES);
    }
    /* -- [ ESSA FUNÇÃO, LINKA O VETOR DE PARTICULA COM O PROGRAMA OPENGL ] --
     * Esse link é feito fazendo com que cada informação seja atribuída ao seu lugar correto
      * evitando assim que as coordendas se mistures, por exemplo cord de cores com direção.*/
    public void bindData(ParticleShaderProgram programaDeParticulas)
    {
        int dadoAtual = 0; // Coordenada do atributo atual (que esta sendo manipulado)
        // --[ Coordenadas de posição ]--
        vertexArray.setVertexAttribPointer(dadoAtual, programaDeParticulas.getLocalPosicao(),COMPONENTE_POSICAO, BYTESTOTAIS_STRIDE);
        dadoAtual += COMPONENTE_POSICAO;
        // --[ Coordenadas de cores ]--
        vertexArray.setVertexAttribPointer(dadoAtual, programaDeParticulas.getLocalCor(), COMPONENTE_COR, BYTESTOTAIS_STRIDE);
        dadoAtual += COMPONENTE_COR;
        // --[ Coordenadas de direção ]--
        vertexArray.setVertexAttribPointer(dadoAtual, programaDeParticulas.getLocalVetorDeDirecao(), COMPONENTE_VETOR, BYTESTOTAIS_STRIDE);
        dadoAtual += COMPONENTE_VETOR;
        // --[ Atributo de tempo de vida ]--
        vertexArray.setVertexAttribPointer(dadoAtual, programaDeParticulas.getLocalTempoInicialParticula(), COMPONENTE_TEMPO_INICIAL_PARTICULA, BYTESTOTAIS_STRIDE);
        dadoAtual += COMPONENTE_TEMPO_INICIAL_PARTICULA;
        // --[ Coordenadas de Extras ]--
        vertexArray.setVertexAttribPointer(dadoAtual, programaDeParticulas.getLocalInformacoesExtras(), PARTICULA_EXTRA_VEC3, BYTESTOTAIS_STRIDE);
        dadoAtual += PARTICULA_EXTRA_VEC3;
    }
    /* --[ FUNÇÃO DESENHO NA TELA ] -- */
    public void desenhar() {
        glDrawArrays(GL_POINTS, 0, particulaAtual);
        /* Obs: As particulas estão sendo presentadas como pontos na tela
        * Dentro do OpenGL (dentro dos arquivos vertex shader), é possivel difinir o tamanho em pixels, controlar
        * influênciar no trajeto, entre outras coisas*/
    }
    /* --[ FUNÇÃO ATRIBUI EXTRA ] -- */
    public void setExtra(Geometry.Point extra)
    {
        this.extra = extra;
    }
}
