export default function LoginPage() {
  return (
    <section className="mx-auto mt-16 max-w-md rounded-lg border border-slate-200 bg-white p-6">
      <h2 className="mb-4 text-xl font-semibold">Sign in</h2>
      <div className="space-y-3">
        <input className="w-full rounded border border-slate-300 px-3 py-2" placeholder="Email" />
        <input className="w-full rounded border border-slate-300 px-3 py-2" placeholder="Password" type="password" />
        <button className="w-full rounded bg-slate-900 px-3 py-2 text-white">Login</button>
      </div>
    </section>
  );
}
