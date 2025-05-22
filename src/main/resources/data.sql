-- Insert roles
INSERT INTO roles(name) VALUES('ROLE_CUSTOMER') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ROLE_ADMIN') ON CONFLICT DO NOTHING;

-- Insert admin user (password: admin123)
INSERT INTO users(names, email, phone, national_id, password)
VALUES('Admin User', 'admin@eucl.com', '0700000000', 'ADMIN123456789', '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi')
ON CONFLICT (email) DO NOTHING;

-- Assign admin role
INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@eucl.com' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;
