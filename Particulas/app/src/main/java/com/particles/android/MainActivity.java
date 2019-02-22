package com.particles.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // --- [ BOTÕES ] ---
    private Button bt_iniciar;
    // --- [ EFEITOS ] ---
    private Spinner comboEfeitos;
    // --- [ TEXTURA ] ---
    private Switch simTextura;
    private Switch naoTextura;
    // --- [ ARQUIVO ] ---
    private static final String ARQUIVO_CONFIGURACAO = "configParametro";
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor arquivoEditor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // --- [ ARQUIVO ] ---
        sharedPreferences = getSharedPreferences(ARQUIVO_CONFIGURACAO,0); //Arquivo e modo de leitura (0 é privado)
        arquivoEditor = sharedPreferences.edit();
        // --- [ BOTÕES ] ---
        bt_iniciar=(Button)findViewById(R.id.bt_iniciar);
        // --- [ EFEITOS ] ---
        comboEfeitos=(Spinner)findViewById(R.id.comboEfeitos);
        List<String> listaEfeitos = new ArrayList<>(Arrays.asList("Chuva","Faisca","Fogo","Fogos","Fonte","Neve"));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaEfeitos );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboEfeitos.setAdapter(dataAdapter);
        // --- [ SELECAO TEXTURA ] ---
        // Link dos objetos Switch
        simTextura = (Switch)findViewById(R.id.sParaDes);
        simTextura.setChecked(true);
        naoTextura = (Switch)findViewById(R.id.sParaLin);
        // --- [ BOTÕES ] ---
        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Grava em arquivo o Efeito escolhido e se o mesmo fará uso de textura ou não*/
                arquivoEditor.putString("efeito",comboEfeitos.getSelectedItem().toString());;
                if(simTextura.isChecked()) {
                    arquivoEditor.putInt("textura",1);
                }
                if(naoTextura.isChecked()) {
                    arquivoEditor.putInt("textura",0);
                }
                arquivoEditor.commit(); // gravando configurações
                // Sai do menu e entra na tela de Partículas
                Intent intent = new Intent(getApplicationContext(), ParticulasActivity.class);
                startActivity(intent);
            }
        });
        simTextura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simTextura.isChecked())
                    naoTextura.setChecked(false);
                else
                    naoTextura.setChecked(true);
            }
        });
        naoTextura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(naoTextura.isChecked())
                    simTextura.setChecked(false);
                else
                    simTextura.setChecked(true);
            }
        });
    }
}
