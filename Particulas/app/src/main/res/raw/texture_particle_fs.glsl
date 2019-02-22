precision mediump float;

varying vec3 v_Color;
varying float v_ElapsedTime;

uniform sampler2D u_TextureUnit;

void main()
{
    /* Adicionando textura ao ponto ao mesmo tempo que se multiplica a
    cor com o mesmo,assim, aplicando a cor a textura*/
    gl_FragColor = vec4(v_Color, 1.0) * texture2D(u_TextureUnit, gl_PointCoord);
}