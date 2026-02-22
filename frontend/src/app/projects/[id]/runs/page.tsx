import Link from 'next/link';
import { apiGet } from '@/lib/api';
import type { TestRun } from '@/types/run';

type PageProps = {
  params: { id: string };
};

export default async function RunsPage({ params }: PageProps) {
  const runs = await apiGet<TestRun[]>(`/api/projects/${params.id}/runs`).catch(() => []);

  return (
    <section>
      <h2 className="mb-4 text-2xl font-bold">Test Runs</h2>
      <div className="space-y-3">
        {runs.map((run) => (
          <article key={run.id} className="rounded border border-slate-200 bg-white p-4">
            <p className="text-xs text-slate-500">{run.runType}</p>
            <h3 className="font-semibold">{run.title}</h3>
            <p className="text-sm text-slate-600">Status: {run.status}</p>
            <Link className="mt-2 inline-block text-sm text-blue-600" href={`/projects/${params.id}/runs/${run.id}`}>
              Open run detail
            </Link>
          </article>
        ))}
      </div>
    </section>
  );
}
