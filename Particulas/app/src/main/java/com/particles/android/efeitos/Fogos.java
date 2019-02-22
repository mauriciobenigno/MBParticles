package com.particles.android.efeitos;

import com.particles.android.objects.SistemaParticulas;
import com.particles.android.util.Geometry;
import com.particles.android.util.Randomizador;
import java.util.Random;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.setRotateEulerM;

public class Fogos {
    private Geometry.Point posicao;
    private final Geometry.Vector direcao;
    private final int cor;
    private final float angulo; // Variação de Angulo
    private final float velocidade; // Variação de velocidade
    private final Random random = new Random(); // Randomizador
    private float[] matrizDeRotacao = new float[16]; // Matriz de rotação
    private float[] vetorDeDirecao = new float[4]; // Matriz de direção
    private float[] vetorResultante = new float[4]; // Matriz resultante
    // -- [ INFORMAÇÕES VEC3 EXTRA ] --
    private Geometry.Point extra;
    // -- [ MÉTODO CONSTRUTOR ] --
    public Fogos(Geometry.Point posicao, Geometry.Vector direcao, int cor, float angulo, float velocidade) {
        this.posicao = posicao;
        this.direcao = direcao;
        this.cor = cor;
        // Atribuindo valores a matriz de direção, velocidade e angulo de variação
        this.angulo = angulo;
        this.velocidade = velocidade;
        vetorDeDirecao[0] = direcao.x;
        vetorDeDirecao[1] = direcao.y;
        vetorDeDirecao[2] = direcao.z;
        //Inicializando os extras
        extra = new Geometry.Point(0.0f,0.0f,3.6f);
    }
    // -- [ ADICIONA PARTICULAS AO SISTEMA DE PARTICULAS ] --
    public void addParticulas(SistemaParticulas sistemaParticulas, float tempoAtual, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            // Altera o angulo para onde a particula ta sendo atirada
            setRotateEulerM(matrizDeRotacao, 0, (random.nextFloat() - 0.5f) * angulo,
                    (random.nextFloat() - 0.5f) * angulo, (random.nextFloat() - 0.5f) * angulo);
            // É feita a multiplicação da matriz para que o vetor fique "rodado"
            multiplyMV(vetorResultante, 0, matrizDeRotacao, 0, vetorDeDirecao, 0);
            // Ajuste de velocidade
            float ajusteVelocidade = 1f + random.nextFloat() * velocidade;
            // Ajuste de angulo
            Geometry.Vector direcaoParticula = new Geometry.Vector(vetorResultante[0] * ajusteVelocidade,
                    vetorResultante[1] * ajusteVelocidade, vetorResultante[2] * ajusteVelocidade);
            // Adicionando particula
            Randomizador a = new Randomizador();
            int rgb = a.randomNumeroInt(255,0); // Vermelho
            rgb = (rgb << 8) + a.randomNumeroInt(255,0); //Verde
            rgb = (rgb << 8) + a.randomNumeroInt(255,0); //Azul
            sistemaParticulas.addParticula(new Geometry.Point(posicao.x,posicao.y,posicao.z), rgb, direcaoParticula, tempoAtual);
        }
        /*Obs: Uma simples estrutura de repetição que adiciona a quantidade definida na fonte de particulas ao sistema*/
    }

    public void setPosicao(Geometry.Point posicao) {
        this.posicao = posicao;
    }

    public void setExtra(Geometry.Point extra) {
        this.extra = extra;
    }
}
