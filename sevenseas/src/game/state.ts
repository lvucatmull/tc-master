import type { GoodsId, Port } from "./data/world";

export type PlayerState = {
  gold: number;
  cargoCapacity: number;
  cargo: Record<GoodsId, number>;
  currentPort: Port | null;
};

export const initialPlayerState: PlayerState = {
  gold: 1000,
  cargoCapacity: 30,
  cargo: {
    fish: 0,
    silk: 0,
    wood: 0
  },
  currentPort: null
};
