'use client';

import { useTestEditorStore } from '@/store/test-editor-store';
import type { TestCase } from '@/types/test';

type TestCaseEditorProps = {
  testCase: TestCase;
};

export function TestCaseEditor({ testCase }: TestCaseEditorProps) {
  const { code, setCode, language, setLanguage } = useTestEditorStore();

  return (
    <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
      <section className="rounded border border-slate-200 bg-white p-4">
        <p className="text-xs text-slate-500">{testCase.tcId}</p>
        <h2 className="mb-2 text-xl font-semibold">{testCase.title}</h2>
        <p className="mb-4 text-sm text-slate-600">{testCase.description ?? 'No description'}</p>
        <div className="space-y-2 text-sm">
          <p><strong>Precondition:</strong> {testCase.precondition ?? '-'}</p>
          <p><strong>Steps:</strong> {testCase.steps ?? '-'}</p>
          <p><strong>Expected:</strong> {testCase.expectedResult ?? '-'}</p>
        </div>
      </section>

      <section className="rounded border border-slate-200 bg-white p-4">
        <div className="mb-3 flex items-center justify-between">
          <h3 className="font-semibold">Code Editor</h3>
          <select
            value={language}
            onChange={(e) => setLanguage(e.target.value as typeof language)}
            className="rounded border border-slate-300 px-2 py-1 text-sm"
          >
            <option value="TS">TypeScript</option>
            <option value="JS">JavaScript</option>
            <option value="PYTHON">Python</option>
            <option value="JAVA">Java</option>
          </select>
        </div>
        <textarea
          className="h-[420px] w-full rounded border border-slate-300 p-3 font-mono text-sm"
          value={code}
          onChange={(e) => setCode(e.target.value)}
        />
      </section>
    </div>
  );
}
