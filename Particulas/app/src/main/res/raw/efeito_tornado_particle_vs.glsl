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


    /*if(spawn.x<-0.5)
    {
            direcao.x =  direcao.x * -1.0;

            //p_Extra.x = p_Extra.x-p_Extra.z;
    }*/
    direcao.x = direcao.x+0.1;
    direcao.y = direcao.y+0.1;
   /* if(v_ElapsedTime>0.5)
        {
            direcao.x = direcao.x-0.1;
            direcao.y = 0.0;
            direcao.z = 0.0;
                //p_Extra.x = p_Extra.x-p_Extra.z;
        }*/
    if(v_ElapsedTime>1.0)
    {
        //direcao.x = -2.0;
        //direcao.y = 3.0;
    }


    /*if(spawn.y>p_Extra.y)
    {
        direcao.x = direcao.x * -1.0;
        p_Extra.y = p_Extra.y+p_Extra.z;
    }*/


    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 8.0;

    vec3 currentPosition = spawn + (direcao * v_ElapsedTime);
    //currentPosition.y -= gravityFactor;

    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    //gl_PointSize = 100.0-gravityFactor;



    gl_PointSize = 55.0;
    //gl_PointSize = p_Extra.x;
}