//
//  PBR_ShaderTexture.vsh
//

#ifdef GL_ES
precision highp float;
#endif

attribute vec3    a_position;
attribute vec3    a_normal;
attribute vec2    a_texCoord0;

uniform mat4      u_projTrans;
uniform mat4      u_worldTrans;

uniform vec3      vLight0;

varying vec2 texCoord;
varying vec4 p;
varying vec3 v_normal;

void main(void)
{
    p = vec4(a_position,1);
    gl_Position = u_projTrans * (u_worldTrans * p);

    texCoord = mod(a_texCoord0,1.0);
    v_normal=a_normal;
}
