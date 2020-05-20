#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

const vec4 fog_colour = vec4(0, 0, 0, 1.0);

vec4 add_fog(vec4 fragColour) {
  float perspective_far = 500.0;
  float fog_coord = (gl_FragCoord.z / gl_FragCoord.w) / perspective_far;
  float fog_density = 0;
  float fog = fog_coord * fog_density;
  return mix(fog_colour, fragColour, clamp(1.0 - fog, 0., 1.) );
}

void main()
{
  vec4 col = v_color * texture2D(u_texture, v_texCoords);
  vec4 fog = add_fog(col);

  gl_FragColor = vec4(fog.rgb, col.a);
}