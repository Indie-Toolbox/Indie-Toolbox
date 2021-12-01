#version 330 core

in vec4 v_Colour;
in vec2 v_TexCoord;
in float v_TexIndex;
in float v_Rounding;
in vec2 v_UiDims;

uniform sampler2D u_TextureSlots[8];

const float smoothness = 1;

float roundCorners() {
	vec2 pixelPos = v_TexCoord * vec2(v_UiDims.x, v_UiDims.y);
	vec2 minCorner = vec2(v_Rounding, v_Rounding);
	vec2 maxCorner = vec2(v_UiDims.x - v_Rounding, v_UiDims.y - v_Rounding);

	vec2 cornerPoint = clamp(pixelPos, minCorner, maxCorner);
	float lowerBound = (v_Rounding - smoothness);
	float upperBound = (v_Rounding + smoothness);

	return 1.0 - smoothstep(lowerBound, upperBound, distance(pixelPos, cornerPoint));
}

void main() {
	int index = int(v_TexIndex);
	gl_FragColor = texture(u_TextureSlots[index], v_TexCoord) * v_Colour;
	gl_FragColor.a *= roundCorners();
}
