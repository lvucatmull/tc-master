import { GOODS, type GoodsId, type Port } from "../data/world";

export function getPortPrice(port: Port, goodsId: GoodsId): number {
  const goods = GOODS.find((item) => item.id === goodsId);
  if (!goods) return 0;
  const modifier = port.specialties[goodsId] ?? 1;
  return Math.round(goods.basePrice * modifier);
}
