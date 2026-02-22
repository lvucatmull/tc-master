export function Header() {
  return (
    <header className="flex h-16 items-center justify-between border-b border-slate-200 bg-white px-6">
      <p className="text-sm font-medium text-slate-600">Test Case Management Platform</p>
      <button className="rounded bg-slate-900 px-3 py-2 text-sm text-white">New Project</button>
    </header>
  );
}
