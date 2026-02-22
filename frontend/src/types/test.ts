export type SuiteNode = {
  id: number;
  projectId: number;
  parentId: number | null;
  name: string;
  description: string | null;
  orderIndex: number;
};

export type TestCase = {
  id: number;
  tcId: string;
  suiteId: number;
  title: string;
  description: string | null;
  precondition: string | null;
  steps: string | null;
  expectedResult: string | null;
  priority: 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW';
  category: string | null;
  tags: string | null;
  status: 'ACTIVE' | 'DEPRECATED' | 'DRAFT';
};

export type TestCode = {
  id: number;
  testCaseId: number;
  language: 'JS' | 'TS' | 'PYTHON' | 'JAVA';
  codeContent: string;
  version: number;
};
