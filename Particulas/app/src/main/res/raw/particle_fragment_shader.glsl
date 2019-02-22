precision mediump float;

varying vec3 v_Color;
varying float v_ElapsedTime;

uniform sampler2D u_TextureUnit;


void main()
{
    //Aplicando cor sem variação ao Fragmento
    gl_FragColor = vec4(v_Color, 1.0);
}