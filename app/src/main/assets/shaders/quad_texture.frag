#version 320 es

precision mediump float;

in VS_OUT {
    vec2 tex_coord;
}fs_in;

out vec4 out_color;
// binding corresponds to the active texture glActiveTexture(GL_TEXTURE0 + 1)
layout(binding = 1) uniform sampler2D tex_object;

void main() {
    out_color = texture(tex_object, fs_in.tex_coord) * vec4(1.0f, 1.0f, 1.0f, 1.0f);

}