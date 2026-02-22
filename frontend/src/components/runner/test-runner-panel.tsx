'use client';

import { useCallback, useState } from 'react';
import { useRunStream } from '@/hooks/use-run-stream';
import type { RunEvent, TestRun } from '@/types/run';

type TestRunnerPanelProps = {
  run: TestRun;
};

export function TestRunnerPanel({ run }: TestRunnerPanelProps) {
  const [events, setEvents] = useState<RunEvent[]>([]);

  const onEvent = useCallback((event: RunEvent) => {
    setEvents((prev) => [...prev, event]);
  }, []);

  useRunStream({ runId: run.id, onEvent });

  return (
    <section className="rounded border border-slate-200 bg-white p-4">
      <div className="mb-3 flex items-center justify-between">
        <h3 className="font-semibold">Test Runner</h3>
        <span className="rounded bg-slate-100 px-2 py-1 text-xs">{run.status}</span>
      </div>
      <div className="h-56 overflow-auto rounded bg-slate-950 p-3 font-mono text-xs text-emerald-300">
        {events.length === 0 ? 'Waiting for run events...' : events.map((event, idx) => (
          <p key={idx}>[{event.timestamp}] {event.message}</p>
        ))}
      </div>
    </section>
  );
}
