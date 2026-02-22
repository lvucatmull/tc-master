'use client';

import { useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import type { RunEvent } from '@/types/run';

type UseRunStreamArgs = {
  runId: number;
  onEvent: (event: RunEvent) => void;
};

export function useRunStream({ runId, onEvent }: UseRunStreamArgs) {
  useEffect(() => {
    if (!runId) return;

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws/runs'),
      reconnectDelay: 2000,
    });

    client.onConnect = () => {
      client.subscribe(`/topic/runs/${runId}`, (message) => {
        onEvent(JSON.parse(message.body) as RunEvent);
      });
    };

    client.activate();
    return () => {
      client.deactivate();
    };
  }, [runId, onEvent]);
}
