CREATE DATABASE IF NOT EXISTS alumni_portal;
USE alumni_portal;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('STUDENT','ALUMNI') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profiles (
  user_id INT PRIMARY KEY,
  graduation_year INT,
  company VARCHAR(100),
  designation VARCHAR(100),
  bio TEXT,
  profile_pic_url VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE opportunities (
  id INT AUTO_INCREMENT PRIMARY KEY,
  posted_by INT,
  title VARCHAR(150) NOT NULL,
  description TEXT,
  posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (posted_by) REFERENCES users(id) ON DELETE SET NULL
);
