#version 150

uniform sampler2D DiffuseSampler;

uniform vec3 Gray;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;
uniform vec3 Offset;
uniform vec3 ColorScale;
uniform float Contrast;
uniform float Saturation;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 InTexel = texture(DiffuseSampler, texCoord);

    // Calculate the luminance of the pixel (brightness)
    float luminance = dot(InTexel.rgb, vec3(0.299, 0.587, 0.114));

    // Check if the luminance is high enough to apply color
    float brightnessThreshold = 0.6; // Adjust this threshold as needed

    vec3 OutColor = InTexel.rgb;

    if (luminance > brightnessThreshold) {
        // Check if the color is predominantly red (red > green and blue)
        float redStrength = InTexel.r;
        float greenStrength = InTexel.g;
        float blueStrength = InTexel.b;

        float colorThreshold = 0.98; // Threshold to check if it's predominantly red

        // If the red component is significantly higher than green and blue, apply the color transformation
        if (redStrength > greenStrength + colorThreshold && redStrength > blueStrength + colorThreshold) {
            // Color Matrix
            float RedValue = dot(OutColor, RedMatrix);
            float GreenValue = dot(OutColor, GreenMatrix);
            float BlueValue = dot(OutColor, BlueMatrix);
            OutColor = vec3(RedValue, GreenValue, BlueValue);

            // Contrast
            OutColor = (OutColor - 0.5) * Contrast + Contrast * 0.5;

            // Saturation
            float Luma = dot(OutColor, Gray);
            vec3 Chroma = OutColor - Luma;
            OutColor = (Chroma * (1. + Chroma * Saturation)) + Luma;
        }
    }

    fragColor = vec4(OutColor, 1.0);
}
