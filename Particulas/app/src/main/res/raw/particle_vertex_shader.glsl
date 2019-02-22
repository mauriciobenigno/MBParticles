uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute vec3 p_Extra;
attribute float a_ParticleStartTime;

varying vec3 v_Color;
varying vec3 direcao;
varying float v_ElapsedTime;

void main()
{
    v_Color = a_Color;
    v_ElapsedTime = u_Time - a_ParticleStartTime;

    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 8.0;

    direcao = a_DirectionVector;

    vec3 currentPosition = a_Position + (direcao * v_ElapsedTime);

    currentPosition.y -= gravityFactor;

    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    gl_PointSize = 25.0;
}