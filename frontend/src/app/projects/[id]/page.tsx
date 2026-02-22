import { PassRateDonut } from '@/components/dashboard/pass-rate-donut';
import { RunTrendChart } from '@/components/dashboard/run-trend-chart';
import { StatCard } from '@/components/dashboard/stat-card';
import { apiGet } from '@/lib/api';
import type { DashboardData, StatsData } from '@/types/dashboard';

type PageProps = {
  params: { id: string };
};

export default async function ProjectDashboardPage({ params }: PageProps) {
  const [dashboard, stats] = await Promise.all([
    apiGet<DashboardData>(`/api/projects/${params.id}/dashboard`).catch(() => ({
      totalRuns: 0,
      totalResults: 0,
      passRate: 0,
      statusCounts: { PASS: 0, FAIL: 0, SKIP: 0, ERROR: 0 },
      trend: [],
    })),
    apiGet<StatsData>(`/api/projects/${params.id}/stats`).catch(() => ({
      projects: 0,
      suites: 0,
      testCases: 0,
      automatedCodes: 0,
    })),
  ]);

  return (
    <section className="space-y-4">
      <h2 className="text-2xl font-bold">Project Dashboard</h2>
      <div className="grid gap-3 md:grid-cols-4">
        <StatCard label="Total Runs" value={dashboard.totalRuns} />
        <StatCard label="Total Results" value={dashboard.totalResults} />
        <StatCard label="Pass Rate" value={`${dashboard.passRate.toFixed(1)}%`} />
        <StatCard label="Test Cases" value={stats.testCases} />
      </div>
      <div className="grid gap-4 lg:grid-cols-2">
        <PassRateDonut
          pass={dashboard.statusCounts.PASS}
          fail={dashboard.statusCounts.FAIL}
          skip={dashboard.statusCounts.SKIP}
          error={dashboard.statusCounts.ERROR}
        />
        <RunTrendChart data={dashboard.trend} />
      </div>
    </section>
  );
}
