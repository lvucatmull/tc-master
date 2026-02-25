export type GoodsId = "fish" | "silk" | "wood";

export type Goods = {
  id: GoodsId;
  name: string;
  basePrice: number;
};

export type Port = {
  id: string;
  name: string;
  x: number;
  y: number;
  specialties: Partial<Record<GoodsId, number>>;
};

export const GOODS: Goods[] = [
  { id: "fish", name: "어류", basePrice: 50 },
  { id: "silk", name: "비단", basePrice: 180 },
  { id: "wood", name: "목재", basePrice: 90 }
];

export const PORTS: Port[] = [
  { id: "lisbon", name: "리스본", x: 180, y: 240, specialties: { fish: 0.8, wood: 1.1, silk: 1.3 } },
  { id: "seville", name: "세비야", x: 420, y: 180, specialties: { fish: 1.1, wood: 0.9, silk: 1.2 } },
  { id: "venice", name: "베네치아", x: 690, y: 260, specialties: { fish: 1.2, wood: 1.3, silk: 0.75 } }
];
