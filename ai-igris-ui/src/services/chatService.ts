import { ChatRequest } from '../types';

const API_BASE = '/ai';

export const chatService = {
  async sendMessage(request: ChatRequest): Promise<string> {
    const response = await fetch(`${API_BASE}/chat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status} ${response.statusText}`);
    }

    return response.text();
  },

  async getConfig(): Promise<{ model: string; baseUrl: string }> {
    const response = await fetch(`${API_BASE}/config`);
    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }
    return response.json();
  },
};
