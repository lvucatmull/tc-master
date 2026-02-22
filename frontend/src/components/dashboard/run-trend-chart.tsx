'use client';

import { Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';

type RunTrendChartProps = {
  data: Array<{
    date: string;
    pass: number;
    fail: number;
    runs: number;
  }>;
};

export function RunTrendChart({ data }: RunTrendChartProps) {
  return (
    <section className="rounded border border-slate-200 bg-white p-4">
      <h3 className="mb-4 font-semibold">Execution Trend</h3>
      <div className="h-64">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={data}>
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="pass" stroke="#16a34a" strokeWidth={2} />
            <Line type="monotone" dataKey="fail" stroke="#dc2626" strokeWidth={2} />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </section>
  );
}
