import Link from 'next/link';

export default function DashboardPage() {
  return (
    <section className="space-y-4">
      <h2 className="text-2xl font-bold">Dashboard</h2>
      <p className="text-slate-600">프로젝트를 선택해 상세 대시보드를 확인하세요.</p>
      <Link href="/projects" className="inline-block rounded bg-slate-900 px-3 py-2 text-sm text-white">
        Go to Projects
      </Link>
    </section>
  );
}
