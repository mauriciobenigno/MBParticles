precision mediump float;

varying vec3 v_Color;
varying float v_ElapsedTime;

uniform sampler2D u_TextureUnit;


void main()
{
    //Aplicando cor com variação ao tempo
    gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0);
}