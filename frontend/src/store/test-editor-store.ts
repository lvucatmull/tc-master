import { create } from 'zustand';

type EditorState = {
  language: 'TS' | 'JS' | 'PYTHON' | 'JAVA';
  code: string;
  setLanguage: (language: EditorState['language']) => void;
  setCode: (code: string) => void;
};

export const useTestEditorStore = create<EditorState>((set) => ({
  language: 'TS',
  code: '// Write your test code',
  setLanguage: (language) => set({ language }),
  setCode: (code) => set({ code }),
}));
