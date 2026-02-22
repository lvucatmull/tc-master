export type TestRun = {
  id: number;
  projectId: number;
  title: string;
  runType: 'FULL' | 'SUITE' | 'SELECTIVE';
  status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'ABORTED';
  startedAt: string | null;
  completedAt: string | null;
};

export type RunResult = {
  id: number;
  testRunId: number;
  testCaseId: number;
  result: 'PASS' | 'FAIL' | 'SKIP' | 'ERROR';
  durationMs: number;
  logOutput: string | null;
  errorMessage: string | null;
  executedAt: string;
};

export type RunEvent = {
  runId: number;
  type: string;
  message: string;
  status: TestRun['status'];
  timestamp: string;
};
