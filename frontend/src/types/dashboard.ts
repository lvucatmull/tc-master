export type DashboardData = {
  totalRuns: number;
  totalResults: number;
  passRate: number;
  statusCounts: Record<'PASS' | 'FAIL' | 'SKIP' | 'ERROR', number>;
  trend: Array<{
    date: string;
    runs: number;
    pass: number;
    fail: number;
  }>;
};

export type StatsData = {
  projects: number;
  suites: number;
  testCases: number;
  automatedCodes: number;
};

export type ReportRow = {
  runTitle: string;
  tcId: string;
  result: string;
  durationMs: number;
  executedAt: string;
};
