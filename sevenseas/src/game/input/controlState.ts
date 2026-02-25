export type ControlState = {
  left: boolean;
  right: boolean;
  up: boolean;
  down: boolean;
  dock: boolean;
};

export const controlState: ControlState = {
  left: false,
  right: false,
  up: false,
  down: false,
  dock: false
};

export function consumeDock(): boolean {
  if (!controlState.dock) return false;
  controlState.dock = false;
  return true;
}
