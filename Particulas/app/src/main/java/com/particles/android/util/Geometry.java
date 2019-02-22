package com.particles.android.util;

public class Geometry {
    // Classe ponto
    public static class Point {
        public float x, y, z;
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Point translateY(float distance)
        {
            return new Point(x, y + distance, z);
        }
        // Translação de ponto
        public Point translate(Vector vector) {
            return new Point(x + vector.x, y + vector.y, z + vector.z);
        }
    }
    // Vetor
    public static class Vector {
        public float x, y, z;
        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        // comprimento
        public float length()
        {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }
        // Produto cruzado
        public Vector crossProduct(Vector other) {
            return new Vector((y * other.z) - (z * other.y), (z * other.x) - (x * other.z), (x * other.y) - (y * other.x));
        }
        // Produto escalar entre dois vetores
        public float dotProduct(Vector other)
        {
            return x * other.x + y * other.y + z * other.z;
        }
        // Scala
        public Vector scale(float f)
        {
            return new Vector(x * f, y * f, z * f);
        }
    }
}

