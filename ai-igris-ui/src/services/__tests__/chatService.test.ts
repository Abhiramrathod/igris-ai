import { describe, it, expect, vi, beforeEach } from 'vitest';
import { chatService } from '../chatService';

describe('chatService', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  describe('sendMessage', () => {
    it('should send POST request and return response text', async () => {
      const mockResponse = 'Hello from AI';
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        text: () => Promise.resolve(mockResponse),
      } as Response);

      const result = await chatService.sendMessage({
        userName: 'guest',
        sessionId: 'sess-1',
        prompt: 'Hi',
      });

      expect(result).toBe(mockResponse);
      expect(globalThis.fetch).toHaveBeenCalledWith('/ai/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userName: 'guest',
          sessionId: 'sess-1',
          prompt: 'Hi',
        }),
      });
    });

    it('should throw on non-ok response', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 500,
        statusText: 'Internal Server Error',
      } as Response);

      await expect(
        chatService.sendMessage({
          userName: 'guest',
          sessionId: 'sess-1',
          prompt: 'Hi',
        })
      ).rejects.toThrow('Error: 500 Internal Server Error');
    });
  });

  describe('getConfig', () => {
    it('should return model and baseUrl', async () => {
      const config = { model: 'gpt-4', baseUrl: 'https://api.openai.com/v1' };
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(config),
      } as Response);

      const result = await chatService.getConfig();

      expect(result).toEqual(config);
      expect(globalThis.fetch).toHaveBeenCalledWith('/ai/config');
    });

    it('should throw on non-ok response', async () => {
      vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false,
        status: 404,
      } as Response);

      await expect(chatService.getConfig()).rejects.toThrow('Error: 404');
    });
  });
});
