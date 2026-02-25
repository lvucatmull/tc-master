import Phaser from "phaser";
import { GOODS, PORTS, type Port } from "../data/world";
import { getPortPrice } from "../systems/economy";
import { initialPlayerState, type PlayerState } from "../state";
import { consumeDock, controlState } from "../input/controlState";

const WORLD_WIDTH = 900;
const WORLD_HEIGHT = 600;
const SHIP_SPEED = 180;
const DOCK_DISTANCE = 52;

type GoodsId = (typeof GOODS)[number]["id"];

export class MainScene extends Phaser.Scene {
  private player = structuredClone(initialPlayerState) as PlayerState;
  private cursors!: Phaser.Types.Input.Keyboard.CursorKeys;
  private dockKey!: Phaser.Input.Keyboard.Key;

  private seaLayer!: Phaser.GameObjects.TileSprite;
  private seaFoamLayer!: Phaser.GameObjects.TileSprite;
  private ship!: Phaser.GameObjects.Sprite;
  private shipWake!: Phaser.GameObjects.Ellipse;
  private nearbyPort: Port | null = null;

  private hudEl = document.getElementById("hud") as HTMLDivElement;
  private panelEl = document.getElementById("trade-panel") as HTMLDivElement;

  constructor() {
    super("main");
  }

  create(): void {
    this.cursors = this.input.keyboard!.createCursorKeys();
    this.dockKey = this.input.keyboard!.addKey(Phaser.Input.Keyboard.KeyCodes.E);

    this.createTextures();
    this.createWorldVisuals();
    this.createPorts();
    this.createShip();

    this.renderHud();
    this.closeTradePanel();
  }

  update(_: number, deltaMs: number): void {
    const delta = deltaMs / 1000;
    const move = new Phaser.Math.Vector2(0, 0);

    const left = this.cursors.left.isDown || controlState.left;
    const right = this.cursors.right.isDown || controlState.right;
    const up = this.cursors.up.isDown || controlState.up;
    const down = this.cursors.down.isDown || controlState.down;

    if (left) move.x -= 1;
    if (right) move.x += 1;
    if (up) move.y -= 1;
    if (down) move.y += 1;

    if (move.lengthSq() > 0) {
      move.normalize();
      this.ship.x = Phaser.Math.Clamp(this.ship.x + move.x * SHIP_SPEED * delta, 16, WORLD_WIDTH - 16);
      this.ship.y = Phaser.Math.Clamp(this.ship.y + move.y * SHIP_SPEED * delta, 16, WORLD_HEIGHT - 16);
      this.ship.rotation = move.angle() + Math.PI / 2;
      this.shipWake.setVisible(true);
      this.shipWake.setPosition(this.ship.x - move.x * 12, this.ship.y - move.y * 12);
      this.shipWake.rotation = this.ship.rotation;
      this.shipWake.scaleY = Phaser.Math.Linear(this.shipWake.scaleY, 1, 0.2);
    } else {
      this.shipWake.scaleY = Phaser.Math.Linear(this.shipWake.scaleY, 0.35, 0.2);
    }

    this.seaLayer.tilePositionX += 9 * delta;
    this.seaLayer.tilePositionY += 3 * delta;
    this.seaFoamLayer.tilePositionX += 22 * delta;

    this.nearbyPort = this.findNearbyPort();

    const dockPressed = Phaser.Input.Keyboard.JustDown(this.dockKey) || consumeDock();
    if (dockPressed && this.nearbyPort) {
      this.player.currentPort = this.nearbyPort;
      this.openTradePanel(this.nearbyPort);
    }

    if (this.nearbyPort) {
      this.hudEl.dataset.prompt = `${this.nearbyPort.name} 입항: E / Dock`;
    } else {
      delete this.hudEl.dataset.prompt;
    }

    this.renderHud();
  }

  private createTextures(): void {
    if (!this.textures.exists("sea-pattern")) {
      const g = this.make.graphics({ x: 0, y: 0, add: false });
      g.fillStyle(0x0b3f63, 1);
      g.fillRect(0, 0, 128, 128);
      g.fillStyle(0x0f557f, 0.55);
      for (let y = 0; y < 128; y += 16) {
        g.fillEllipse(20 + (y % 32), y + 8, 36, 8);
        g.fillEllipse(90 - (y % 28), y + 12, 28, 7);
      }
      g.generateTexture("sea-pattern", 128, 128);
      g.clear();

      g.fillStyle(0xb5e4ff, 0.22);
      for (let y = 0; y < 128; y += 20) {
        g.fillEllipse(30 + (y % 24), y + 8, 42, 6);
        g.fillEllipse(98 - (y % 20), y + 6, 30, 5);
      }
      g.generateTexture("foam-pattern", 128, 128);
      g.clear();

      g.fillStyle(0x6f4b2a, 1);
      g.fillTriangle(18, 34, 30, 8, 42, 34);
      g.fillStyle(0x5b3d24, 1);
      g.fillRect(27, 12, 6, 20);
      g.fillStyle(0xe8dcc5, 1);
      g.fillTriangle(30, 13, 30, 27, 42, 20);
      g.lineStyle(2, 0xdcbf8c, 1);
      g.strokeRoundedRect(14, 28, 32, 12, 5);
      g.generateTexture("player-ship", 60, 44);
      g.destroy();
    }
  }

  private createWorldVisuals(): void {
    this.add.rectangle(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, WORLD_WIDTH, WORLD_HEIGHT, 0x082941, 1);

    this.seaLayer = this.add.tileSprite(
      WORLD_WIDTH / 2,
      WORLD_HEIGHT / 2,
      WORLD_WIDTH,
      WORLD_HEIGHT,
      "sea-pattern"
    );
    this.seaFoamLayer = this.add
      .tileSprite(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, WORLD_WIDTH, WORLD_HEIGHT, "foam-pattern")
      .setAlpha(0.45);

    const vignette = this.add.rectangle(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, WORLD_WIDTH, WORLD_HEIGHT, 0x000000, 0.16);
    vignette.setBlendMode(Phaser.BlendModes.MULTIPLY);
  }

  private createPorts(): void {
    PORTS.forEach((port) => {
      this.add.ellipse(port.x - 18, port.y + 18, 120, 64, 0x8c6e43, 0.95);
      this.add.ellipse(port.x - 6, port.y + 12, 96, 48, 0x588a4a, 0.85);

      const glow = this.add.circle(port.x, port.y, 10, 0xf3c878, 0.9);
      this.add
        .tween({
          targets: glow,
          alpha: { from: 0.2, to: 0.9 },
          scale: { from: 0.9, to: 1.4 },
          duration: 1200,
          repeat: -1,
          yoyo: true,
          ease: "Sine.InOut"
        });

      this.add.circle(port.x, port.y, 5, 0x1f2c3d, 1);
      this.add.text(port.x - 32, port.y + 20, port.name, {
        fontFamily: "Georgia, serif",
        fontSize: "15px",
        color: "#fff1c8",
        stroke: "#31210f",
        strokeThickness: 4
      });
    });
  }

  private createShip(): void {
    this.ship = this.add.sprite(240, 320, "player-ship").setScale(0.9);
    this.shipWake = this.add.ellipse(this.ship.x, this.ship.y + 12, 16, 36, 0xd0f3ff, 0.26).setVisible(false);
  }

  private findNearbyPort(): Port | null {
    for (const port of PORTS) {
      const distance = Phaser.Math.Distance.Between(this.ship.x, this.ship.y, port.x, port.y);
      if (distance <= DOCK_DISTANCE) return port;
    }
    return null;
  }

  private cargoUsed(): number {
    return Object.values(this.player.cargo).reduce((sum, count) => sum + count, 0);
  }

  private buy(goodsId: GoodsId): void {
    const port = this.player.currentPort;
    if (!port) return;

    const price = getPortPrice(port, goodsId);
    if (this.player.gold < price) return;
    if (this.cargoUsed() >= this.player.cargoCapacity) return;

    this.player.gold -= price;
    this.player.cargo[goodsId] += 1;
    this.openTradePanel(port);
    this.renderHud();
  }

  private sell(goodsId: GoodsId): void {
    const port = this.player.currentPort;
    if (!port) return;
    if (this.player.cargo[goodsId] <= 0) return;

    const price = getPortPrice(port, goodsId);
    this.player.gold += price;
    this.player.cargo[goodsId] -= 1;
    this.openTradePanel(port);
    this.renderHud();
  }

  private openTradePanel(port: Port): void {
    this.panelEl.classList.remove("hidden");

    const goodsRows = GOODS.map((goods) => {
      const price = getPortPrice(port, goods.id);
      const owned = this.player.cargo[goods.id];
      return `
        <div class="trade-row">
          <p><b>${goods.name}</b> | 시세 ${price} | 보유 ${owned}</p>
          <button data-buy="${goods.id}">매수</button>
          <button data-sell="${goods.id}">매도</button>
        </div>
      `;
    }).join("");

    this.panelEl.innerHTML = `
      <h3>${port.name} 교역소</h3>
      <p>남은 적재량: ${this.player.cargoCapacity - this.cargoUsed()} / ${this.player.cargoCapacity}</p>
      ${goodsRows}
      <button data-close="1">출항(닫기)</button>
    `;

    this.panelEl.querySelectorAll("[data-buy]").forEach((el) => {
      el.addEventListener("click", () => this.buy(el.getAttribute("data-buy") as GoodsId));
    });

    this.panelEl.querySelectorAll("[data-sell]").forEach((el) => {
      el.addEventListener("click", () => this.sell(el.getAttribute("data-sell") as GoodsId));
    });

    this.panelEl.querySelector("[data-close]")?.addEventListener("click", () => this.closeTradePanel());
  }

  private closeTradePanel(): void {
    this.player.currentPort = null;
    this.panelEl.classList.add("hidden");
    this.panelEl.innerHTML = "";
  }

  private renderHud(): void {
    const prompt = this.hudEl.dataset.prompt ? `<br/>${this.hudEl.dataset.prompt}` : "";
    this.hudEl.innerHTML = `
      자금: ${this.player.gold}<br/>
      화물: ${this.cargoUsed()} / ${this.player.cargoCapacity}${prompt}
    `;
  }
}
