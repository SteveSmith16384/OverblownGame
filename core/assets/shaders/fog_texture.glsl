
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_diffuseTexture;
uniform vec2 u_tilemapOffset;
uniform vec2 u_tilemapSize;
uniform vec2 u_textureRepeat;

varying vec2 v_texCoord0;


const vec4 fog_colour = vec4(0, 0, 0, 1.);

vec4 add_fog(vec4 fragColour) {
  float perspective_far = 500.0;
  float fog_coord = (gl_FragCoord.z / gl_FragCoord.w) / perspective_far;
  float fog_density = 0;
  float fog = fog_coord * fog_density;
  return mix(fog_colour, fragColour, clamp(1.0 - fog, 0., 1.) );
}


void main() {
    vec2 tex0 = fract(v_texCoord0 * u_textureRepeat);

    vec2 tex = (tex0 + u_tilemapOffset) / u_tilemapSize;
    vec4 col = texture2D(u_diffuseTexture, tex);

    gl_FragColor = add_fog(col);
}