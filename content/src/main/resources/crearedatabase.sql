CREATE TABLE posts (
                       id INT AUTO_INCREMENT PRIMARY KEY,        -- Primary key với auto-increment
                       post_title VARCHAR(600) NOT NULL,         -- Tiêu đề bài viết (tối đa 600 ký tự)
                       post_content TEXT NOT NULL,               -- Nội dung bài viết (kiểu TEXT)
                       rejection_reason VARCHAR(255),            -- Lý do từ chối (nếu có)
                       post_status TINYINT,                      -- Trạng thái bài viết (Enum dạng số nguyên)
                       user_id INT,                              -- ID của người dùng đăng bài
                       is_delete BOOLEAN NOT NULL DEFAULT FALSE, -- Đánh dấu bài viết có bị xóa hay không
                       created_at DATETIME NOT NULL,             -- Thời gian tạo bài viết
                       updated_at DATETIME NOT NULL              -- Thời gian cập nhật bài viết
);
CREATE TABLE comments (
                          id INT AUTO_INCREMENT PRIMARY KEY,        -- Primary key với auto-increment
                          comment_content TEXT NOT NULL,            -- Nội dung bình luận (kiểu TEXT)
                          comment_parent_id INT,                    -- ID của bình luận cha (nếu có)
                          post_id INT,                              -- ID bài viết (foreign key tới posts)
                          user_id INT,                              -- ID của người dùng bình luận
                          is_delete BOOLEAN NOT NULL DEFAULT FALSE, -- Đánh dấu bình luận có bị xóa hay không
                          created_at DATETIME NOT NULL,             -- Thời gian tạo bình luận
                          updated_at DATETIME NOT NULL,             -- Thời gian cập nhật bình luận
                          CONSTRAINT fk_post_comment FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);
CREATE TABLE likes (
                       id INT AUTO_INCREMENT PRIMARY KEY, -- Primary key với auto-increment
                       user_id INT NOT NULL,              -- ID của người dùng thích bài viết
                       post_id INT,                       -- ID bài viết (foreign key tới posts)
                       CONSTRAINT fk_post_like FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);
