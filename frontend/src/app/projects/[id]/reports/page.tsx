import { apiGet } from '@/lib/api';
import type { ReportRow } from '@/types/dashboard';

type PageProps = {
  params: { id: string };
};

export default async function ReportsPage({ params }: PageProps) {
  const reports = await apiGet<ReportRow[]>(`/api/projects/${params.id}/reports`).catch(() => []);

  return (
    <section className="space-y-4">
      <h2 className="text-2xl font-bold">Reports</h2>
      <a
        className="inline-block rounded bg-slate-900 px-3 py-2 text-sm text-white"
        href={`http://localhost:8080/api/projects/${params.id}/reports/csv`}
      >
        Export CSV
      </a>
      <div className="overflow-hidden rounded border border-slate-200 bg-white">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50">
            <tr>
              <th className="px-3 py-2">Run</th>
              <th className="px-3 py-2">TC ID</th>
              <th className="px-3 py-2">Result</th>
              <th className="px-3 py-2">Duration</th>
              <th className="px-3 py-2">Executed At</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((row, idx) => (
              <tr key={`${row.tcId}-${idx}`} className="border-t border-slate-200">
                <td className="px-3 py-2">{row.runTitle}</td>
                <td className="px-3 py-2">{row.tcId}</td>
                <td className="px-3 py-2">{row.result}</td>
                <td className="px-3 py-2">{row.durationMs}ms</td>
                <td className="px-3 py-2">{row.executedAt}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
