# Alumni Networking Portal

Cloud-hosted platform for alumni and students to connect, network, and share opportunities.

**Stack:** Java (Spring Boot) · MySQL (RDS) · AWS EC2 · AWS S3 · HTML/CSS/JS · JWT auth

## Structure

```
alumni-portal/
├── backend/          Spring Boot REST API (deploy to EC2)
├── frontend/          Static HTML/CSS/JS (deploy to S3)
└── database/
    └── schema.sql     MySQL schema (run on RDS)
```

## Running locally

### 1. Database
```bash
mysql -u root -p < database/schema.sql
```

### 2. Backend
```bash
cd backend
export DB_HOST=localhost
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
export JWT_SECRET=$(openssl rand -base64 32)
export S3_BUCKET_NAME=your-bucket-name
export AWS_REGION=us-east-1

mvn spring-boot:run
```
Backend runs at `http://localhost:8080`.

### 3. Frontend
Edit `frontend/js/config.js` and point `API_BASE_URL` to `http://localhost:8080/api`.
Then open `frontend/index.html` in a browser, or serve it:
```bash
cd frontend
python3 -m http.server 5500
```

## Deploying to AWS

See the accompanying deployment guide for full steps:
1. Create IAM role for EC2 with least-privilege S3/RDS access.
2. Create an RDS MySQL instance, enable encryption at rest, run `schema.sql`.
3. Launch EC2, attach the IAM role, install Java 17, upload the built JAR (`mvn clean package`), run it (ideally as a systemd service behind Nginx with HTTPS).
4. Update `SecurityConfig.java` CORS origin and `frontend/js/config.js` API URL to match your real domains.
5. Create an S3 bucket, enable static website hosting, upload the `frontend/` contents, apply a public-read bucket policy scoped to that bucket only.
6. (Optional) Put CloudFront in front of S3 and Route 53 for a custom domain with HTTPS on both ends.

## Security notes

- Passwords are hashed with BCrypt — never stored in plaintext.
- Authentication uses stateless JWTs (`Authorization: Bearer <token>`), validated by `JwtAuthFilter`.
- The EC2 instance uses an IAM role (not hardcoded access keys) to reach S3/RDS.
- DB credentials and the JWT secret are injected via environment variables — keep them out of source control and consider AWS Secrets Manager for production.
- Enable RDS "encryption at rest" and terminate HTTPS in front of both EC2 and S3/CloudFront for encryption in transit.
