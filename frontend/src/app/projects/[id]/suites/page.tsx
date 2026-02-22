import { apiGet } from '@/lib/api';
import { TreeView } from '@/components/tree/tree-view';
import type { SuiteNode } from '@/types/test';

type PageProps = {
  params: { id: string };
};

export default async function SuitesPage({ params }: PageProps) {
  const suites = await apiGet<SuiteNode[]>(`/api/projects/${params.id}/suites`).catch(() => []);

  return (
    <section className="space-y-4">
      <h2 className="text-2xl font-bold">Suites</h2>
      <TreeView projectId={params.id} suites={suites} />
    </section>
  );
}
