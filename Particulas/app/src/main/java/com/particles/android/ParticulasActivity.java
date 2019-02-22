package com.particles.android;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ParticulasActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    // --- [ ARQUIVO ] ---
    private static final String ARQUIVO_CONFIGURACAO = "configParametro"; //constant
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor arquivoEditor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particulas);
        // --- [ ARQUIVO ] ---
        sharedPreferences = getSharedPreferences(ARQUIVO_CONFIGURACAO,0); //Arquivo e modo de leitura (0 é privado)
        arquivoEditor = sharedPreferences.edit();
        // GL Surface
        glSurfaceView = new GLSurfaceView(getApplicationContext());
        /* Pegar configurações do dispositivo e verificar se suporta Opengl ES 2 ou superior */
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        /* pega informações do dispositivo a partir do contexto. */
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        /* Verifica se o dispositivo suporta openGL a partir da versão digitada e guarda um resultado bool */
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        //final ParticlesRenderer particlesRenderer = new ParticlesRenderer(this);
        final ParticlesRenderer particlesRenderer = new ParticlesRenderer(getApplicationContext());
        if (supportsEs2) {
            // Seta a informação de qual versão do OpenGL será usada na superfície
            glSurfaceView.setEGLContextClientVersion(2);
            // Inicia a superfície de renderização
            glSurfaceView.setRenderer(particlesRenderer);
            rendererSet = true;
        }
        else { // não suporta OpenGL ES  da versão
            Toast.makeText(getApplicationContext(), "Esse dispositivo não suporta OpenGL ES 2.0 ou superior.", Toast.LENGTH_LONG).show();
            return;
        }
        glSurfaceView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    /* Converte as coordenadas de pixel para coordenadas reconhecidas pelo OpenGL
                     * Obs: No Android as coordenadas de pixel são invertidas em relação ao OpenGL */
                    float cordX = (float) v.getWidth()/2.6f;
                    cordX = event.getX()/cordX;
                    cordX = cordX-1.3f;
                    final float coordenadaGeometricaX = cordX;
                    /* 2,6 foi o intervalo encontrado entre o menor valor vizivel em X e o maior (-1,3 a 1,3)*/
                    float cordY = (float) v.getHeight()/4.2f;
                    cordY = ((event.getY()-v.getHeight())*-1.0f)/cordY;
                    cordY = cordY-0.6f;
                    final float coordenadaGeometricaY = cordY;
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                particlesRenderer.handleTouchDrag(coordenadaGeometricaX, coordenadaGeometricaY);
                            }
                        });
                    }
                    return true;
                } else {
                    return false;
                }
            }});
        Toast.makeText(getApplicationContext(), "Para alterar a simulação" +
                "\nAperte a tecla VOLTAR para retornar" +
                "\nao menu de configurações.", Toast.LENGTH_SHORT).show();
        setContentView(glSurfaceView);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }
}
