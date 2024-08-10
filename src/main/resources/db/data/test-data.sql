INSERT INTO balance (account_id, category, total_amount) VALUES
    ('123', 0, 1000.00),
    ('123', 1, 1000.00),
    ('123', 2, 1000.00) ON CONFLICT DO NOTHING;