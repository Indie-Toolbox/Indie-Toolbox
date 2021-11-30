#version 330 core

layout (location = 0) in vec2 a_Position;
layout (location = 1) in vec2 a_TexCoord;
layout (location = 2) in vec4 a_Colour;
layout (location = 3) in float a_TexIndex;
layout (location = 4) in float a_Rounding;
layout (location = 5) in vec2 a_UiDims;

uniform mat4 u_Projection;

out vec4 v_Colour;
out vec2 v_TexCoord;
out float v_TexIndex;
out float v_Rounding;
out vec2 v_UiDims;

void main() {
	gl_Position = u_Projection * vec4(a_Position, 0.0, 1.0);
	v_Colour = a_Colour;
	v_TexCoord = a_TexCoord;
	v_TexIndex = a_TexIndex;
	v_Rounding = a_Rounding;
	v_UiDims = a_UiDims;
}