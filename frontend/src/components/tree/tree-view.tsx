import Link from 'next/link';
import type { SuiteNode } from '@/types/test';

type TreeViewProps = {
  projectId: string;
  suites: SuiteNode[];
};

export function TreeView({ projectId, suites }: TreeViewProps) {
  return (
    <div className="rounded border border-slate-200 bg-white p-4">
      <h3 className="mb-3 text-sm font-semibold text-slate-700">Test Suites</h3>
      <ul className="space-y-2">
        {suites.map((suite) => (
          <li key={suite.id}>
            <Link
              href={`/projects/${projectId}/suites?suiteId=${suite.id}`}
              className="block rounded px-2 py-1 text-sm hover:bg-slate-100"
            >
              {suite.name}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
