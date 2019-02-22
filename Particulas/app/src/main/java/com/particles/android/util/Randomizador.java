package com.particles.android.util;

import java.util.Random;

public class Randomizador {

    public Randomizador() {
    }

    public float randomNumero(float max, float min) {
        Random r = new Random();
        return (r.nextFloat()*(max-min)+min);
    }
    public int randomNumeroInt(int max, int min) {
        Random r = new Random();
        return (r.nextInt(max-min)+min);
    }
}
