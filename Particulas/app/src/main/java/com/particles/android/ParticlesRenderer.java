package com.particles.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.Log;


import com.particles.android.objects.Particula;
import com.particles.android.objects.SistemaParticulas;
import com.particles.android.programs.ParticleShaderProgram;
import com.particles.android.util.Geometry;
import com.particles.android.util.MatrixHelper;
import com.particles.android.util.Randomizador;
import com.particles.android.util.TextureHelper;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class ParticlesRenderer implements GLSurfaceView.Renderer
{
    // Contexto ou tela atual que o Android está executando
    private final Context context;
    // Matrizes de projeção para a correta visualização da tela
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    // Objetos que fazem referencia ao Sistema de Particulas e os códigos de Shader
    private ParticleShaderProgram particleProgram;
    private SistemaParticulas sistemaParticulas;
    // Fontes de partículas
    private Particula [] particula = new Particula[5];
    // Variavel de medida de tempo
    private long globalStartTime;
    // Aplicação de textura na particula para melhorar a visualização
    private int texture;
    // Identificar se houve toque na tela
    boolean toqueDisplay;
    // --- [ ARQUIVO ] ---
    private static final String ARQUIVO_CONFIGURACAO = "configParametro"; //constant
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor arquivoEditor;
    String tipoEfeito;
    int usoTextura;
    Geometry.Vector direcaoParticulas;
    // Método construtor
    public ParticlesRenderer(Context context) {
        this.context = context;
        // inicializando a classe que gravará o arquivo
        sharedPreferences = context.getSharedPreferences(ARQUIVO_CONFIGURACAO,0); //Arquivo e modo de leitura (0 é privado)
        arquivoEditor = sharedPreferences.edit();
        toqueDisplay=false;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Limpa a cor de fundo para a cor indicada 0,0,0
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Permite a mistura de cores
        glEnable(GL_BLEND);
        // Dita a forma como a mistura vai acontecer
        glBlendFunc(GL_ONE, GL_ONE);
        // -- [ Recupera alguns dados de arquivo ] --
        tipoEfeito = sharedPreferences.getString("efeito","Fogos");
        usoTextura = sharedPreferences.getInt("textura",1);
        // -- [ Inicializa o Sistema de particulas e o Shader ] --
        switch (this.tipoEfeito) {
            case "Chuva":
                if(usoTextura==0) { //Não usa textura
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_chuva_particle_vs,
                            R.raw.particle_fragment_shader);
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_pingo);
                }
                else {
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_pingo);
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_chuva_particle_vs,
                            R.raw.texture_particle_fs);
                }
                direcaoParticulas = new Geometry.Vector(-2.0f,-3.0f,0.0f);
                sistemaParticulas = new SistemaParticulas(3000);
                break;
            case "Faisca":
                if(usoTextura==0) { //Não usa textura
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_faisca_vs, R.raw.particle_fs_com_tempo);
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_pingo);
                }
                else {
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_pingo);
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_faisca_vs, R.raw.particle_fs_com_tempo);
                }
                direcaoParticulas = new Geometry.Vector(-2.0f,-3.0f,0.0f);
                sistemaParticulas = new SistemaParticulas(500);
                break;
            case "Fogo":
                if(usoTextura==0) { //Não usa textura
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_fogo_particle_vs,
                            R.raw.particle_fragment_shader);
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_fumaca);
                }
                else {
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_fogo_particle_vs, R.raw.texture_particle_fs);
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_fumaca);
                }
                direcaoParticulas = new Geometry.Vector(1.3f,-2.0f,0.0f);
                sistemaParticulas = new SistemaParticulas(5000);
                break;
            case "Fogos":
                if(usoTextura==0) { //Não usa textura
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_pingo);
                    particleProgram = new ParticleShaderProgram(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);
                }
                else {
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_pingo);
                    particleProgram = new ParticleShaderProgram(context, R.raw.particle_vertex_shader, R.raw.texture_particle_fs);
                }
                direcaoParticulas = new Geometry.Vector(0.0f,3.0f,0.0f);
                sistemaParticulas = new SistemaParticulas(3000);
                break;
            case "Fonte":
                Log.d("PARTICLEPROGRAM","Fonte");
                if(usoTextura==0)  //Não usa textura
                    particleProgram = new ParticleShaderProgram(context,R.raw.particle_vertex_shader,R.raw.particle_fs_com_tempo);
                else
                    particleProgram = new ParticleShaderProgram(context,R.raw.particle_vertex_shader,R.raw.particle_fs_com_tempo);

                direcaoParticulas = new Geometry.Vector(0.0f,0.5f,0.0f);
                sistemaParticulas = new SistemaParticulas(5000);
                break;
            case "Neve":
                if(usoTextura==0) { //Não usa textura
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_neve_particle_vs, R.raw.particle_fragment_shader);
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_snow);
                }
                else {
                    particleProgram = new ParticleShaderProgram(context, R.raw.efeito_neve_particle_vs, R.raw.texture_particle_fs);
                    texture = TextureHelper.loadTexture(context, R.drawable.texture_snow);
                }
                direcaoParticulas = new Geometry.Vector(1.0f,-3.0f,0.0f);
                sistemaParticulas = new SistemaParticulas(5000);
                break;
        }
        // -- [ Inicializa a variavel de controle de tempo ] --
        globalStartTime = System.nanoTime();
        // -- [ Inicializa as fontes de particulas ] --
        switch (this.tipoEfeito) {
            case "Chuva":
                particula [0] = new Particula(tipoEfeito,new Geometry.Point(0f, 3.8f, 0f), direcaoParticulas,
                        Color.rgb(226, 243, 255), 3f, -2);
                break;
            case "Faisca":
                particula [0] = new Particula(tipoEfeito,new Geometry.Point(0f, 3.8f, 0f), direcaoParticulas,
                        Color.rgb(255, 171, 0), 360, 0.5f);
                break;
            case "Fogo":
                particula [0] = new Particula(tipoEfeito,new Geometry.Point(0f, 0.4f, 0f), direcaoParticulas,
                        Color.rgb(226, 88, 34), 180, 0.15f);
                particula [1] = new Particula(tipoEfeito,new Geometry.Point(0f, 0.2f, 0f), direcaoParticulas,
                        Color.rgb(226, 88, 34), 180, 0.15f);
                particula [2] = new Particula(tipoEfeito,new Geometry.Point(0f, -0.2f, 0f), direcaoParticulas,
                        Color.rgb(226, 88, 34), 180, 0.15f);
                particula [3] = new Particula(tipoEfeito,new Geometry.Point(0f, -0.6f, 0f), direcaoParticulas,
                        Color.rgb(226, 88, 34), 180, 0.25f);
                break;
            case "Fogos":
                particula [0] = new Particula(tipoEfeito,new Geometry.Point(0f, 3.8f, 0f), direcaoParticulas,
                        Color.rgb(255, 171, 0), 360, 0.02f);
                break;
            case "Fonte":
                particula [0] = new Particula(tipoEfeito,new Geometry.Point(0f, 0.0f, 0f), direcaoParticulas,
                        Color.rgb(0,127,255), 10f, 1f);
                particula [1] = new Particula(tipoEfeito,new Geometry.Point(1.1f, 0.0f, 0f),
                        new Geometry.Vector(-0.1f,0.2f,0.0f),
                        Color.rgb(0,127,255), 1f, 1f);
                particula [2] = new Particula(tipoEfeito,new Geometry.Point(-1.1f, 0.0f, 0f),
                        new Geometry.Vector(0.1f,0.2f,0.0f),
                        Color.rgb(0,127,255), 1f, 1f);
                break;
            case "Neve":
                particula [0] = new Particula(tipoEfeito,new Geometry.Point(0f, 3.8f, 0f), direcaoParticulas,
                        Color.rgb(226, 243, 255), 3f, 0.2f);
                break;
        }
        /* No momento que os objetos são instanciados, são informadas as coordenas ponto de partida,
        * bem como sua direção e por fim a cor.
        * Obs: A posição de partida também pode variar, isso irá depender do tipo de simulação selecionada.*/
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        /* Os dados aqui são arranjados de forma a definir uma visualização padrão*/
     }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Limpa a superficie de renderização, para que possa ser desenhada novamente
        glClear(GL_COLOR_BUFFER_BIT);
        // Faz a conversão de nano segundos para segundos
        float tempoAtual = (System.nanoTime() - globalStartTime) / 1000000000f;
        /* Informa ao Objeto que fará uso do sistema de particulas, qual sistema utiliza
        * qual o tempo atual e quantas particulas devem ser criadas*/
        switch (this.tipoEfeito) {
            case "Chuva":
                particula[0].addParticulas(sistemaParticulas, tempoAtual, 5);
                break;
            case "Faisca":
                if(toqueDisplay) {
                    Randomizador randomizador = new Randomizador();
                    particula[0].addParticulas(sistemaParticulas, tempoAtual, randomizador.randomNumeroInt(20,5));
                }
                toqueDisplay=false;
                break;
            case "Fogo":
                particula[0].addParticulas(sistemaParticulas, tempoAtual, 1);
                particula[1].addParticulas(sistemaParticulas, tempoAtual, 2);
                particula[2].addParticulas(sistemaParticulas, tempoAtual, 5);
                particula[3].addParticulas(sistemaParticulas, tempoAtual, 10);
                break;
            case "Fogos":
                if(toqueDisplay) {
                    Randomizador randomizador = new Randomizador();
                    particula[0].addParticulas(sistemaParticulas, tempoAtual, randomizador.randomNumeroInt(300,150));
                }
                toqueDisplay=false;
                break;
            case "Fonte":
                particula[0].addParticulas(sistemaParticulas, tempoAtual, 5);
                particula[1].addParticulas(sistemaParticulas, tempoAtual, 1);
                particula[2].addParticulas(sistemaParticulas, tempoAtual, 1);
                break;
            case "Neve":
                particula[0].addParticulas(sistemaParticulas, tempoAtual, 2);
                break;
        }
        // vincula o Sistema de particulas com o Shader de OpenGL
        particleProgram.useProgram();
        // verificar o que é
        particleProgram.setUniforms(viewProjectionMatrix, tempoAtual, texture);
        // Organiza a ordem dos dados para o programa OpenGL
        sistemaParticulas.bindData(particleProgram);
        // Da a ordem de desenhar as particulas
        sistemaParticulas.desenhar();
    }
    /* -- [ Capturando coordenadas em arrasto de dedo na tela ] --*/
    public void handleTouchDrag(float coordX, float coordY) {
        switch (this.tipoEfeito) {
            case "Chuva":
                break;
            case "Faisca":
                particula[0].setPosicao(new Geometry.Point(coordX,coordY,0.0f));
                toqueDisplay=true;
                break;
            case "Fogo":
                particula[0].setPosicao(new Geometry.Point(coordX,coordY+1.0f,0.0f));
                particula[1].setPosicao(new Geometry.Point(coordX,coordY+0.6f,0.0f));
                particula[2].setPosicao(new Geometry.Point(coordX,coordY+0.4f,0.0f));
                particula[3].setPosicao(new Geometry.Point(coordX,coordY,0.0f));
                break;
            case "Fogos":
                particula[0].setPosicao(new Geometry.Point(coordX,coordY,0.0f));
                toqueDisplay=true;
                break;
            case "Fonte":
                particula[0].setExtra(new Geometry.Point(coordX,coordY,0.0f));
                break;
            case "Neve":
                break;
        }
    }
}