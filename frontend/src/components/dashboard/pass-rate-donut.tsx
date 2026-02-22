'use client';

import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts';

type PassRateDonutProps = {
  pass: number;
  fail: number;
  skip: number;
  error: number;
};

const COLORS = ['#16a34a', '#dc2626', '#f59e0b', '#7c3aed'];

export function PassRateDonut({ pass, fail, skip, error }: PassRateDonutProps) {
  const data = [
    { name: 'Pass', value: pass },
    { name: 'Fail', value: fail },
    { name: 'Skip', value: skip },
    { name: 'Error', value: error },
  ];

  return (
    <section className="rounded border border-slate-200 bg-white p-4">
      <h3 className="mb-4 font-semibold">Pass Rate</h3>
      <div className="h-64">
        <ResponsiveContainer width="100%" height="100%">
          <PieChart>
            <Pie data={data} dataKey="value" innerRadius={60} outerRadius={90}>
              {data.map((entry, index) => (
                <Cell key={entry.name} fill={COLORS[index]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </div>
    </section>
  );
}
