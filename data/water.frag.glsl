#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform sampler2D texture;
uniform vec2 resolution;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform float rotation;
uniform float yTranslation;

void main() {
  vec2 repeat = vec2(2, 2);
  vec2 coord = vertTexCoord.st;
  
  float sin_factor = sin(rotation);
  float cos_factor = cos(rotation); 

  vec2 uv = vertTexCoord.st * repeat.xy;
  uv -= repeat.xy * 0.5;

  uv = mat2(cos_factor, sin_factor, -sin_factor, cos_factor) * uv;

  uv += repeat.xy * 0.5;
  uv += vec2(cos_factor, sin_factor) * yTranslation;
  uv = mod(uv, 1.0);

  gl_FragColor = texture2D(texture, uv) * vertColor;
}