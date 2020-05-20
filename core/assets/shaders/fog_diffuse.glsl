
#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 u_diffuseColor;

const vec4 fog_colour = vec4(0, 0, 0, 1.);

vec4 add_fog(vec4 fragColour) {
  float perspective_far = 500.0;
  float fog_coord = (gl_FragCoord.z / gl_FragCoord.w) / perspective_far;
  float fog_density = 0;
  float fog = fog_coord * fog_density;
  return mix(fog_colour, fragColour, clamp(1.0 - fog, 0., 1.) );
}


void main() {
    //gl_FragColor = add_fog(u_diffuseColor);
	return u_diffuseColor;
}