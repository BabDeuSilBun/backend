ALTER TABLE category
MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
MODIFY updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

--매장이 필터되는 카테고리
INSERT INTO category (name) VALUES
('족발·보쌈'),
('찜·탕·찌개'),
('돈까스·회·일식'),
('피자'),
('고기·구이'),
('치킨'),
('중식'),
('아시안'),
('카페·디저트'),
('패스트푸드'),
('분식')
;