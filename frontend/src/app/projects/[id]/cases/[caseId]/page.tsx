import { TestCaseEditor } from '@/components/editor/test-case-editor';
import { apiGet } from '@/lib/api';
import type { TestCase } from '@/types/test';

type PageProps = {
  params: { caseId: string };
};

export default async function CaseDetailPage({ params }: PageProps) {
  const testCase = await apiGet<TestCase>(`/api/cases/${params.caseId}`).catch(() => ({
    id: 0,
    tcId: 'TCM-0',
    suiteId: 0,
    title: 'Sample Test Case',
    description: 'Backend not connected yet.',
    precondition: '',
    steps: '1. Open app',
    expectedResult: 'Page is shown',
    priority: 'MEDIUM' as const,
    category: 'UI',
    tags: '[]',
    status: 'ACTIVE' as const,
  }));

  return <TestCaseEditor testCase={testCase} />;
}
