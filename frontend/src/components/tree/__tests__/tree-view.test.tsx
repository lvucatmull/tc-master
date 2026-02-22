import { render, screen } from '@testing-library/react';
import type { ReactNode } from 'react';
import { describe, expect, it, vi } from 'vitest';
import { TreeView } from '@/components/tree/tree-view';

vi.mock('next/link', () => ({
  default: ({ children, href }: { children: ReactNode; href: string }) => <a href={href}>{children}</a>,
}));

describe('TreeView', () => {
  it('renders suite links', () => {
    render(
      <TreeView
        projectId="1"
        suites={[
          { id: 1, projectId: 1, parentId: null, name: 'Auth', description: null, orderIndex: 0 },
          { id: 2, projectId: 1, parentId: null, name: 'Checkout', description: null, orderIndex: 1 },
        ]}
      />
    );

    expect(screen.getByText('Auth')).toBeInTheDocument();
    expect(screen.getByText('Checkout')).toBeInTheDocument();
  });
});
