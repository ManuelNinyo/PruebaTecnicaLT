# CI/CD Pipeline Configuration

This project includes a comprehensive GitHub Actions CI/CD pipeline for automated testing, building, security scanning, and deployment.

## Pipeline Overview

The pipeline is triggered on:
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

## Pipeline Stages

### 1. Test Stage
- **Environment**: Ubuntu with PostgreSQL 15
- **Actions**:
  - Sets up Java 21 (Temurin distribution)
  - Runs Maven tests with PostgreSQL integration
  - Generates test reports using JUnit
- **Database**: Uses PostgreSQL container for integration tests

### 2. Build Stage
- **Actions**:
  - Builds the application using Maven
  - Creates JAR artifact
  - Uploads build artifacts (retention: 30 days)

### 3. Security Scan Stage
- **Actions**:
  - Runs OWASP Dependency Check
  - Identifies security vulnerabilities in dependencies
  - Uploads security reports

### 4. Docker Build Stage
- **Conditions**: Only runs on `main` branch pushes
- **Actions**:
  - Downloads build artifacts
  - Builds Docker image using existing Dockerfile
  - Pushes to Docker Hub (if not a pull request)
  - Tags images with branch, commit SHA, and `latest` for main branch

### 5. Deploy Stage
- **Conditions**: Only runs on `main` branch pushes
- **Environment**: Production
- **Actions**: Placeholder for deployment commands

## Required Secrets

Configure these secrets in your GitHub repository settings:

1. **DOCKER_USERNAME**: Docker Hub username
2. **DOCKER_PASSWORD**: Docker Hub password or access token
3. **Additional secrets** for deployment (as needed)

## Environment Variables

- `JAVA_VERSION`: Set to '21'
- `MAVEN_OPTS`: Configured with 2GB heap size

## Database Configuration for Tests

The pipeline uses PostgreSQL for integration tests with these settings:
- Database: `testdb`
- Username: `postgres`
- Password: `postgres`
- Port: 5432

## Docker Image Tags

Images are tagged as:
- `{branch-name}` (e.g., `main`, `develop`)
- `{branch-name}-{commit-sha}` (e.g., `main-abc123`)
- `latest` (only for main branch)

## Artifacts

- **Application JAR**: Available for 30 days
- **Security Reports**: Available for 30 days
- **Test Reports**: Generated and displayed in GitHub Actions

## Customization

### Adding New Environments
1. Update the `on` section to include new branches
2. Add new job stages as needed
3. Configure environment-specific secrets

### Modifying Docker Configuration
1. Update the `docker-build` job in `.github/workflows/ci-cd.yml`
2. Modify Docker Hub repository name in metadata action
3. Adjust tagging strategy as needed

### Adding Deployment Steps
1. Configure deployment environments in GitHub repository settings
2. Add deployment commands to the `deploy` job
3. Set up necessary secrets and environment variables

## Troubleshooting

### Test Failures
- Check PostgreSQL container logs
- Verify database connection settings
- Review test reports in GitHub Actions

### Build Failures
- Check Java version compatibility
- Verify Maven dependencies
- Review build logs for specific errors

### Docker Build Issues
- Verify Dockerfile exists and is valid
- Check Docker Hub credentials
- Review build context and file permissions

## Security Considerations

- OWASP Dependency Check runs on every pipeline execution
- Secrets are never logged or exposed in artifacts
- Docker images are scanned during build process
- Production deployments require manual approval (configurable)
