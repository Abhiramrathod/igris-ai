import React, { useState, useRef, useEffect } from 'react';
import { Message } from '../types';
import { chatService } from '../services/chatService';
import './Chat.css';

const Chat: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [model, setModel] = useState('loading...');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    chatService.getConfig().then((config) => {
      setModel(config.model);
    }).catch(() => {
      setModel('unknown');
    });
  }, []);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!input.trim() || isLoading) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      content: input.trim(),
      role: 'user',
      timestamp: new Date(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setInput('');
    setIsLoading(true);

    try {
      const response = await chatService.sendMessage({
        userName: 'guest',
        sessionId: crypto.randomUUID(),
        prompt: userMessage.content,
      });

      const assistantMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: response || 'No response received.',
        role: 'assistant',
        timestamp: new Date(),
      };

      setMessages((prev) => [...prev, assistantMessage]);
    } catch (error) {
      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: `Error: ${error instanceof Error ? error.message : 'Failed to get response'}`,
        role: 'assistant',
        timestamp: new Date(),
      };
      setMessages((prev) => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="igris-app">
      {/* Particles */}
      <div className="shadow-particles">
        {Array.from({ length: 20 }).map((_, i) => (
          <div key={i} className="particle" style={{
            left: `${Math.random() * 100}%`,
            animationDelay: `${Math.random() * 8}s`,
            animationDuration: `${5 + Math.random() * 7}s`,
          }} />
        ))}
      </div>

      <div className="soul-particles">
        {Array.from({ length: 10 }).map((_, i) => (
          <div key={i} className="soul-orb" style={{
            left: `${10 + Math.random() * 80}%`,
            animationDelay: `${Math.random() * 6}s`,
            animationDuration: `${4 + Math.random() * 5}s`,
          }} />
        ))}
      </div>

      <div className="chat-container">
        {/* Header */}
        <header className="igris-header">
          <div className="header-content">
            <div className="logo-section">
              <div className="igris-emblem">
                <div className="emblem-ring" />
                <div className="emblem-ring ring-2" />
                <div className="emblem-core">
                  <span className="emblem-text">I</span>
                </div>
              </div>
              <div className="header-text">
                <h1 className="igris-title">
                  <span className="title-shadow">SHADOW</span>
                  <span className="title-main">IGRIS</span>
                </h1>
                <p className="igris-subtitle">COMMAND CENTER</p>
              </div>
            </div>
            <div className="header-right">
              <div className="model-badge">
                <span className="model-icon">⚡</span>
                <span className="model-name">{model}</span>
              </div>
              <div className="rank-badge">
                <span className="rank-icon">⚔</span>
                <span className="rank-text">S-RANK</span>
              </div>
              <div className="status-indicator">
                <div className="status-dot" />
                <span>ACTIVE</span>
              </div>
            </div>
          </div>
          <div className="header-glow" />
        </header>

        {/* Messages */}
        <div className="messages-container">
          {messages.length === 0 && (
            <div className="empty-state">
              <div className="empty-emblem">
                <div className="empty-ring" />
                <div className="empty-ring ring-2" />
                <div className="empty-ring ring-3" />
                <div className="empty-core" />
              </div>
              <h2 className="empty-title">SHADOW ARMY</h2>
              <p className="empty-text">The Shadow Monarch awaits your command</p>
              <div className="empty-divider" />
              <p className="empty-hint">Speak, and IGRIS shall obey</p>
            </div>
          )}
          
          {messages.map((message, index) => (
            <div
              key={message.id}
              className={`message ${message.role}`}
              style={{ animationDelay: `${index * 0.05}s` }}
            >
              <div className={`message-avatar ${message.role}`}>
                {message.role === 'user' ? (
                  <div className="avatar-user">
                    <span className="user-crown">👑</span>
                    <span>M</span>
                  </div>
                ) : (
                  <div className="avatar-igris">
                    <span>I</span>
                    <div className="avatar-glow" />
                  </div>
                )}
              </div>
              <div className="message-bubble">
                <div className="bubble-header">
                  <span className="bubble-name">
                    {message.role === 'user' ? 'MONARCH' : 'IGRIS'}
                  </span>
                  <span className="bubble-badge">
                    {message.role === 'user' ? '👑' : '⚔'}
                  </span>
                </div>
                <div className="message-text">{message.content}</div>
                <div className="message-meta">
                  <span className="message-time">
                    {message.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                  </span>
                </div>
              </div>
            </div>
          ))}

          {isLoading && (
            <div className="message assistant loading">
              <div className="message-avatar assistant">
                <div className="avatar-igris active">
                  <span>I</span>
                  <div className="avatar-glow active" />
                </div>
              </div>
              <div className="message-bubble thinking">
                <div className="bubble-header">
                  <span className="bubble-name thinking-name">IGRIS</span>
                  <span className="thinking-status">PROCESSING</span>
                </div>
                <div className="thinking-indicator">
                  <div className="thinking-bar" />
                </div>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Input */}
        <form className="input-section" onSubmit={handleSubmit}>
          <div className="input-wrapper">
            <div className="input-icon">⚔</div>
            <input
              type="text"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Command IGRIS..."
              disabled={isLoading}
              className="chat-input"
            />
            <button
              type="submit"
              disabled={isLoading || !input.trim()}
              className="send-button"
            >
              <div className="button-content">
                <svg viewBox="0 0 24 24" fill="none" className="send-icon">
                  <path d="M22 2L11 13" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
                  <path d="M22 2L15 22L11 13L2 9L22 2Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                </svg>
              </div>
              <div className="button-glow" />
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Chat;
