#version 330 core

in vec4 v_Colour;
in vec2 v_TexCoord;
in float v_TexIndex;
in float v_Rounding;
in vec2 v_UiDims;

uniform sampler2D u_TextureSlots[8];

const float smoothness = 0.5;

float roundCorners() {
	vec2 pixelPos = v_TexCoord * vec2(v_UiDims.x, v_UiDims.y);
	vec2 minCorner = vec2(v_Rounding, v_Rounding);
	vec2 maxCorner = vec2(v_UiDims.x - v_Rounding, v_UiDims.y - v_Rounding);

	vec2 cornerPoint = clamp(pixelPos, minCorner, maxCorner);
	float lowerBound = (v_Rounding - smoothness);
	float upperBound = (v_Rounding + smoothness);

	float ppxmin = 1.0 - step(minCorner.x, pixelPos.x);
	float ppxmax = 1.0 - step(pixelPos.x, maxCorner.x);
	float ppymin = 1.0 - step(minCorner.y, pixelPos.y);
	float ppymax = 1.0 - step(pixelPos.y, maxCorner.y);

	float boolean = step(1, (ppxmin + ppxmax) * (ppymin + ppymax));
	float cornerAlpha = 1.0 - smoothstep(lowerBound, upperBound, distance(pixelPos, cornerPoint));
	return boolean * cornerAlpha + (1. - boolean);
}

out vec4 fragColor;
void main() {
	int index = int(v_TexIndex);
	switch (index) {
		case 0: fragColor = texture(u_TextureSlots[0], v_TexCoord); break;
		case 1: fragColor = texture(u_TextureSlots[1], v_TexCoord); break;
		case 2: fragColor = texture(u_TextureSlots[2], v_TexCoord); break;
		case 3: fragColor = texture(u_TextureSlots[3], v_TexCoord); break;
		case 4: fragColor = texture(u_TextureSlots[4], v_TexCoord); break;
		case 5: fragColor = texture(u_TextureSlots[5], v_TexCoord); break;
		case 6: fragColor = texture(u_TextureSlots[6], v_TexCoord); break;
		case 7: fragColor = texture(u_TextureSlots[7], v_TexCoord); break;
	}
	fragColor *= v_Colour;
	fragColor.a *= roundCorners();
}
