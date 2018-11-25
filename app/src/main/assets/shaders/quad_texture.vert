#version 320 es


layout(location = 0) in vec4 position;
layout(location = 1) in vec2 tex_coord;

layout(location = 4) uniform mat4 projection_matrix;
layout(location = 8) uniform mat4 modelview_matrix;

out VS_OUT {
    vec2 tex_coord;
}vs_out;

void main() {
    vs_out.tex_coord = tex_coord;
    gl_Position = projection_matrix * modelview_matrix * position;
}
