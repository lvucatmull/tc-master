import { TestRunnerPanel } from '@/components/runner/test-runner-panel';
import { apiGet } from '@/lib/api';
import type { RunResult, TestRun } from '@/types/run';

type PageProps = {
  params: { runId: string };
};

export default async function RunDetailPage({ params }: PageProps) {
  const run = await apiGet<TestRun>(`/api/runs/${params.runId}`).catch(() => ({
    id: Number(params.runId),
    projectId: 0,
    title: 'Sample Run',
    runType: 'FULL' as const,
    status: 'PENDING' as const,
    startedAt: null,
    completedAt: null,
  }));

  const results = await apiGet<RunResult[]>(`/api/runs/${params.runId}/results`).catch(() => []);

  return (
    <section className="space-y-4">
      <h2 className="text-2xl font-bold">{run.title}</h2>
      <TestRunnerPanel run={run} />
      <section className="rounded border border-slate-200 bg-white p-4">
        <h3 className="mb-2 font-semibold">Results</h3>
        <ul className="space-y-2 text-sm">
          {results.map((result) => (
            <li key={result.id} className="flex justify-between rounded bg-slate-50 px-3 py-2">
              <span>TC #{result.testCaseId}</span>
              <span>{result.result}</span>
            </li>
          ))}
        </ul>
      </section>
    </section>
  );
}
