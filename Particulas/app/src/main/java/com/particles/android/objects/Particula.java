package com.particles.android.objects;

import com.particles.android.efeitos.Chuva;
import com.particles.android.efeitos.Faisca;
import com.particles.android.efeitos.Fogo;
import com.particles.android.efeitos.Fogos;
import com.particles.android.efeitos.Fonte;
import com.particles.android.efeitos.Neve;
import com.particles.android.util.Geometry;

public class Particula {
    private String tipoEfeito;
    // -- [ EFEITOS ] --
    private Chuva efeitoChuva;
    private Faisca efeitoFaisca;
    private Fogo efeitoFogo;
    private Fogos efeitoFogos;
    private Fonte efeitoFonte;
    private Neve efeitoNeve;
    // -- [ MÉTODO CONSTRUTOR ] --
    public Particula(String tipoEfeito,Geometry.Point posicao, Geometry.Vector direcao, int cor, float angulo, float velocidade) {
        this.tipoEfeito = new String(tipoEfeito);
        switch (this.tipoEfeito) {
            case "Chuva":
                efeitoChuva = new Chuva(posicao,direcao,cor,angulo,velocidade);
                break;
            case "Faisca":
                efeitoFaisca = new Faisca(posicao,direcao,cor,angulo,velocidade);
                break;
            case "Fogo":
                efeitoFogo = new Fogo(posicao,direcao,cor,angulo,velocidade);
                break;
            case "Fogos":
                efeitoFogos = new Fogos(posicao,direcao,cor,angulo,velocidade);
                break;
            case "Fonte":
                efeitoFonte = new Fonte(posicao,direcao,cor,angulo,velocidade);
                break;
            case "Neve":
                efeitoNeve = new Neve(posicao,direcao,cor,angulo,velocidade);
                break;
        }
    }
    // -- [ ADICIONA PARTICULAS AO SISTEMA DE PARTICULAS ] --
    public void addParticulas(SistemaParticulas sistemaParticulas, float tempoAtual, int quantidade) {
        switch (this.tipoEfeito) {
            case "Chuva":
                efeitoChuva.addParticulas(sistemaParticulas,tempoAtual,quantidade);
                break;
            case "Faisca":
                efeitoFaisca.addParticulas(sistemaParticulas,tempoAtual,quantidade);
                break;
            case "Fogo":
                efeitoFogo.addParticulas(sistemaParticulas,tempoAtual,quantidade);
                break;
            case "Fogos":
                efeitoFogos.addParticulas(sistemaParticulas,tempoAtual,quantidade);
                break;
            case "Fonte":
                efeitoFonte.addParticulas(sistemaParticulas,tempoAtual,quantidade);
                break;
            case "Neve":
                efeitoNeve.addParticulas(sistemaParticulas,tempoAtual,quantidade);
                break;
        }
    }

    public void setPosicao(Geometry.Point posicao) {
        switch (this.tipoEfeito) {
            case "Faisca":
                efeitoFaisca.setPosicao(posicao);
                break;
            case "Fogo":
                efeitoFogo.setPosicao(posicao);
                break;
            case "Fogos":
                efeitoFogos.setPosicao(posicao);
                break;
            case "Fonte":
                efeitoFonte.setPosicao(posicao);
                break;
        }
    }

    public void setExtra(Geometry.Point extra) {
        switch (this.tipoEfeito) {
            case "Chuva":
                efeitoChuva.setExtra(extra);
                break;
            case "Faisca":
                efeitoFaisca.setExtra(extra);
                break;
            case "Fogo":
                efeitoFogo.setExtra(extra);
                break;
            case "Fogos":
                efeitoFogo.setExtra(extra);
                break;
            case "Fonte":
                efeitoFonte.setExtra(extra);
                break;
        }
    }
}
