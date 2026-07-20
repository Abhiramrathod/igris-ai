import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Chat from '../Chat';
import { chatService } from '../../services/chatService';

vi.mock('../../services/chatService', () => ({
  chatService: {
    sendMessage: vi.fn(),
    getConfig: vi.fn(),
  },
}));

describe('Chat', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (chatService.getConfig as ReturnType<typeof vi.fn>).mockResolvedValue({
      model: 'gpt-4',
      baseUrl: 'https://api.openai.com/v1',
    });
  });

  it('should render header with title', async () => {
    render(<Chat />);
    expect(screen.getByText('SHADOW')).toBeInTheDocument();
    expect(screen.getByText('IGRIS')).toBeInTheDocument();
    expect(screen.getByText('COMMAND CENTER')).toBeInTheDocument();
  });

  it('should display model name after config loads', async () => {
    render(<Chat />);
    await waitFor(() => {
      expect(screen.getByText('gpt-4')).toBeInTheDocument();
    });
  });

  it('should display unknown when config fails', async () => {
    (chatService.getConfig as ReturnType<typeof vi.fn>).mockRejectedValue(
      new Error('Network error')
    );
    render(<Chat />);
    await waitFor(() => {
      expect(screen.getByText('unknown')).toBeInTheDocument();
    });
  });

  it('should show empty state when no messages', () => {
    render(<Chat />);
    expect(screen.getByText('SHADOW ARMY')).toBeInTheDocument();
    expect(screen.getByText('The Shadow Monarch awaits your command')).toBeInTheDocument();
  });

  it('should send message and display response', async () => {
    const user = userEvent.setup();
    (chatService.sendMessage as ReturnType<typeof vi.fn>).mockResolvedValue(
      'AI response'
    );

    render(<Chat />);

    const input = screen.getByPlaceholderText('Command IGRIS...');
    await user.type(input, 'Hello IGRIS');
    await user.keyboard('{Enter}');

    await waitFor(() => {
      expect(screen.getByText('Hello IGRIS')).toBeInTheDocument();
    });

    expect(chatService.sendMessage).toHaveBeenCalledWith(
      expect.objectContaining({
        userName: 'guest',
        prompt: 'Hello IGRIS',
      })
    );

    await waitFor(() => {
      expect(screen.getByText('AI response')).toBeInTheDocument();
    });
  });

  it('should display error message on failure', async () => {
    const user = userEvent.setup();
    (chatService.sendMessage as ReturnType<typeof vi.fn>).mockRejectedValue(
      new Error('Service unavailable')
    );

    render(<Chat />);

    const input = screen.getByPlaceholderText('Command IGRIS...');
    await user.type(input, 'Test');
    await user.keyboard('{Enter}');

    await waitFor(() => {
      expect(screen.getByText(/Error: Service unavailable/)).toBeInTheDocument();
    });
  });

  it('should not send empty message', async () => {
    const user = userEvent.setup();
    render(<Chat />);

    await user.click(screen.getByPlaceholderText('Command IGRIS...'));
    await user.keyboard('{Enter}');

    expect(chatService.sendMessage).not.toHaveBeenCalled();
  });
});
