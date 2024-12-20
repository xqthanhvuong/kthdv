CREATE TABLE reports (
                         id INT AUTO_INCREMENT PRIMARY KEY,      -- Primary key với auto-increment, dùng kiểu INT
                         report_content TEXT NOT NULL,           -- TEXT cho nội dung báo cáo
                         post_id INT,                            -- post_id dùng kiểu INT
                         user_id INT,                            -- user_id dùng kiểu INT
                         created_at DATETIME NOT NULL,           -- Thời gian tạo
                         updated_at DATETIME NOT NULL            -- Thời gian cập nhật
);
