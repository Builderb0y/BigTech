//this code can be run at https://www.shadertoy.com/new

int getType(vec2 uv) {
	float smaller = min(abs(uv.x), abs(uv.y));
	if (smaller < 1.0) return 0;
	if (smaller < 4.0) return 1;
	return 2;
}

float animate(float x, float time) {
	float pulse = sin(x * PI - time * PI) * 0.5 + 0.5;
	pulse *= pulse;
	pulse *= pulse;
	return mix(1.0 - x, 1.0, pulse) * 0.5 + 0.5;
}

float dimNoise(vec2 uv) {
	return hash12(uv) * 0.0625 + 0.9375;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
	vec2 rawUV = floor(fragCoord) + 0.5;
	float time = floor(rawUV.y / 16.0) / -20.0 + 1.0;
	vec2 uv = mod(rawUV, 16.0) - 8.0;
	int type = getType(uv);
	vec3 color;
	if (rawUV.x > 32.0 || rawUV.y > 40.0 * 16.0) {
		color = vec3(0.0);
	}
	else if (type == 0) {
		color = vec3(animate(max(abs(uv.x), abs(uv.y)) * 0.125, time), 0.0, 0.0);
		color.r *= dimNoise(uv);
		if (rawUV.x > 16.0) color.r *= 0.5;
	}
	else if (type == 1) {
		float brightness = 0.3125;
		if (
			getType(uv + vec2(-1.0, 0.0)) != 1 ||
			getType(uv + vec2(-1.0, 1.0)) != 1 ||
			getType(uv + vec2( 0.0, 1.0)) != 1
		) {
			brightness += 0.0625;
		}
		if (
			getType(uv + vec2(1.0,  0.0)) != 1 ||
			getType(uv + vec2(1.0, -1.0)) != 1 ||
			getType(uv + vec2(0.0, -1.0)) != 1
		) {
			brightness -= 0.0625;
		}
		color = vec3(brightness + (hash12(uv) * 2.0 - 1.0) * 0.015625);
	}
	else {
		color = vec3(animate(unmix(8.0, 4.0, min(abs(uv.x), abs(uv.y))), time), 0.0, 0.0);
		color.r *= dimNoise(uv);
		if (rawUV.x > 16.0) color.r *= 0.5;
	}
	fragColor = vec4(color, 1.0);
}