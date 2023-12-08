//this code can be run on https://www.shadertoy.com/new.

const float PI = 3.14159265359;
const float TAU = PI * 2.0;

float square(float x) { return x * x; }

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
	fragCoord.y = iResolution.y - fragCoord.y;
	fragCoord -= vec2(16.0);
	vec2 uv = floor(fragCoord) + 0.5;
	uv.y = min(uv.y, mod(uv.y, 16.0));
	float time = floor(fragCoord.y / 16.0) * (TAU / 16.0);

	float brightness;
	if (uv.x >= 0.0 && uv.x < 16.0 && uv.y >= 0.0 && fragCoord.y < 256.0) {
		float x = uv.x * (TAU / 16.0);
		float  forward = cos(x - time) * 0.5 + 0.5;
		float backward = cos(x + time) * 0.5 + 0.5;
		if (uv.y < 2.0) {
			brightness = forward * 0.5 + 0.5;
		}
		else if (uv.y < 4.0) {
			brightness = (1.0 - (1.0 - square(forward)) * (1.0 - square(backward))) * 0.5 + 0.5;
		}
		else if (uv.y >= 14.0 && uv.x < 2.0) {
			brightness = cos(time) * 0.25 + 0.75;
		}
		else {
			brightness = 0.0;
		}
	}
	else {
		brightness = 0.0;
	}

	fragColor = vec4(vec3(brightness), 1.0);
}