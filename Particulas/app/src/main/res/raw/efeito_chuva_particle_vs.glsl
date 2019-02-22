uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;
attribute vec3 p_Extra;

varying vec3 v_Color;
varying vec3 direcao;
varying vec3 spawn;
varying float v_ElapsedTime;


void main()
{
    v_Color = a_Color;
    v_ElapsedTime = u_Time - a_ParticleStartTime;

    direcao = a_DirectionVector;

    spawn = a_Position;

    vec3 currentPosition = spawn + (direcao * v_ElapsedTime);

    gl_Position = u_Matrix * vec4(currentPosition, 1.0);

    gl_PointSize = p_Extra.x;
}