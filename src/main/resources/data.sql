-- Insert users
INSERT INTO users (username, email, belt_rank, stripe_count, join_date)
VALUES
    ('Joseph', 'jdoe@example.com', 'White', 2, '2025-01-15'),
    ('asmith', 'asmith@example.com', 'Blue', 1, '2024-11-10'),
    ('mkim', 'mkim@example.com', 'Purple', 0, '2023-09-05'),
    ('rlin', 'rlin@example.com', 'Brown', 3, '2022-05-20'),
    ('tnguyen', 'tnguyen@example.com', 'Black', 4, '2020-02-12');

-- Insert sessions
INSERT INTO sessions (user_id, class_date, class_type, duration, notes)
VALUES
    (1, '2025-09-01', 'Gi', 60, 'Worked on guard passing.'),
    (1, '2025-09-05', 'NoGi', 75, 'Focus on leg locks.'),
    (2, '2025-09-02', 'Gi', 90, 'Drilled half-guard sweeps.'),
    (3, '2025-09-03', 'NoGi', 60, 'Positional sparring from side control.'),
    (4, '2025-09-04', 'Gi', 120, 'Competition prep.'),
    (5, '2025-09-06', 'Gi', 90, 'Taught advanced class.'),
    (2, '2025-09-07', 'NoGi', 80, 'Reviewed takedowns.'),
    (3, '2025-09-08', 'Gi', 70, 'Worked on open guard retention.');
