-- Tạo bảng invalidated_token
CREATE TABLE invalidated_token (
                                   id VARCHAR(255) PRIMARY KEY,               -- id là khóa chính
                                   expiry_time TIMESTAMP                      -- expiryTime là thời gian hết hạn
);

-- Tạo bảng users
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- id là khóa chính, tự động tăng
                       username VARCHAR(255) NOT NULL UNIQUE,     -- username là duy nhất và không được null
                       password VARCHAR(255) NOT NULL,            -- password không được null
                       status VARCHAR(255),                       -- status có thể null
                       name VARCHAR(255),                         -- name có thể null
                       avatar VARCHAR(500),                       -- avatar có thể null
                       is_delete BOOLEAN DEFAULT FALSE,           -- isDelete, mặc định là FALSE
                       is_block BOOLEAN DEFAULT FALSE,            -- isBlock, mặc định là FALSE
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- createdAt mặc định là thời gian hiện tại
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- updatedAt
                       dob DATE,                                  -- dob là ngày sinh
                       live_in VARCHAR(255),                      -- liveIn có thể null
                       from_location VARCHAR(255),                -- fromLocation có thể null
                       bio TEXT                                   -- bio là một trường dạng văn bản lớn
);

-- Đảm bảo rằng bảng users có các trường phù hợp với các yêu cầu về unique và nullable.
