import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { StatCard } from '@/components/dashboard/stat-card';

describe('StatCard', () => {
  it('renders label and value', () => {
    render(<StatCard label="Total Runs" value={12} />);

    expect(screen.getByText('Total Runs')).toBeInTheDocument();
    expect(screen.getByText('12')).toBeInTheDocument();
  });
});
