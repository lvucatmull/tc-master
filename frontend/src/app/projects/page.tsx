const mockProjects = [
  { id: 1, code: 'TCM', name: 'TC Master' },
];

export default function ProjectsPage() {
  return (
    <section>
      <h2 className="mb-4 text-2xl font-bold">Projects</h2>
      <div className="grid gap-3">
        {mockProjects.map((project) => (
          <article key={project.id} className="rounded border border-slate-200 bg-white p-4">
            <p className="text-xs text-slate-500">{project.code}</p>
            <h3 className="font-semibold">{project.name}</h3>
          </article>
        ))}
      </div>
    </section>
  );
}
