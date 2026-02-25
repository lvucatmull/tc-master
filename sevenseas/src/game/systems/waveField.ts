export type WaveSample = {
  height: number;
  flowX: number;
  flowY: number;
};

const clamp = (value: number, min: number, max: number): number => Math.min(max, Math.max(min, value));

export class WaveField {
  private readonly f1 = 0.0105;
  private readonly f2 = 0.014;
  private readonly f3 = 0.0075;

  sample(x: number, y: number, timeSec: number): WaveSample {
    const t = timeSec;

    const h1 = Math.sin((x + t * 45) * this.f1);
    const h2 = Math.sin((y - t * 32) * this.f2 + 0.8);
    const h3 = Math.sin((x + y + t * 55) * this.f3 + 1.4);
    const height = (h1 + h2 + h3) / 3;

    const flowX =
      Math.cos((x + t * 45) * this.f1) * this.f1 +
      Math.cos((x + y + t * 55) * this.f3 + 1.4) * this.f3;
    const flowY =
      Math.cos((y - t * 32) * this.f2 + 0.8) * this.f2 +
      Math.cos((x + y + t * 55) * this.f3 + 1.4) * this.f3;

    return {
      height: clamp(height, -1, 1),
      flowX,
      flowY
    };
  }
}
