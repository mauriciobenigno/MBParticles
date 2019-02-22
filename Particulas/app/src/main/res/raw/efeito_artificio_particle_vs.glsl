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
    //spawn = vec3((noise1((p_Extra.y+p_Extra.y))-p_Extra.x),p_Extra.z,0.0);

    if(v_ElapsedTime>0.2)
    {

        //direcao.x = 1.3;
        /*spawn.x=0.0;
        spawn.y=0.0;
        spawn.z=0.0;
        */
    }

/*
    if(u_Time>20 && u_Time<25.0)
        {
            direcao.x = -1.3;
            spawn.x=1.0;
            spawn.y=1.0;
            spawn.z=0.0;
        }*/


    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 8.0;

    //vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);
    //vec3 currentPosition = a_Position + (direcao * v_ElapsedTime);
    //vec3 currentPosition = spawn + (direcao * v_ElapsedTime);



    vec3 currentPosition = spawn + (direcao * v_ElapsedTime);

    currentPosition.y -= gravityFactor;

    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    //gl_PointSize = 25.0;
    gl_PointSize = p_Extra.x;
}