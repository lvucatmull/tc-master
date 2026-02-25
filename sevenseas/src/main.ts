import Phaser from "phaser";
import { MainScene } from "./game/scenes/MainScene";
import { controlState } from "./game/input/controlState";
import "./styles/main.css";

const config: Phaser.Types.Core.GameConfig = {
  type: Phaser.AUTO,
  width: 900,
  height: 600,
  parent: "app",
  backgroundColor: "#0b3656",
  scene: [MainScene],
  scale: {
    mode: Phaser.Scale.FIT,
    autoCenter: Phaser.Scale.CENTER_BOTH
  }
};

new Phaser.Game(config);

const bindPressable = (selector: string, key: keyof typeof controlState): void => {
  const el = document.querySelector(selector) as HTMLButtonElement | null;
  if (!el) return;

  const press = (e: Event) => {
    e.preventDefault();
    controlState[key] = true;
  };

  const release = (e: Event) => {
    e.preventDefault();
    controlState[key] = false;
  };

  el.addEventListener("touchstart", press, { passive: false });
  el.addEventListener("mousedown", press);
  el.addEventListener("touchend", release);
  el.addEventListener("mouseup", release);
  el.addEventListener("mouseleave", release);
};

bindPressable("[data-ctl='left']", "left");
bindPressable("[data-ctl='right']", "right");
bindPressable("[data-ctl='up']", "up");
bindPressable("[data-ctl='down']", "down");

document.querySelector("[data-ctl='dock']")?.addEventListener("click", () => {
  controlState.dock = true;
});
