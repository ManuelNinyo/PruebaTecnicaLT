# GitHub Pages Deployment Guide

## Overview
This client application is configured to automatically deploy to GitHub Pages when pushed to the main/master branch.

## Configuration Required

### 1. Set GitHub Secrets
In your GitHub repository, go to Settings → Secrets and variables → Actions and add:

- `PRODUCTION_API_URL`: Your production API URL (e.g., `https://your-api-domain.com/api`)

### 2. Enable GitHub Pages
1. Go to Settings → Pages in your GitHub repository
2. Set Source to "GitHub Actions"

### 3. API URL Configuration
The application uses environment variables for the API URL:

- **Development**: Uses `http://localhost:8080/api` (configured in `.env.development`)
- **Production**: Uses the URL from `VITE_API_URL` environment variable (set in GitHub Actions)

## Environment Files

- `.env.development` - Development configuration
- `.env.production` - Production configuration (template)
- `.env.example` - Example configuration

## Deployment Process

The CI/CD pipeline will:
1. Run tests on the backend
2. Build the React application for production
3. Deploy to GitHub Pages
4. Use the production API URL from GitHub Secrets

## Local Development

1. Copy `.env.example` to `.env.development`:
   ```bash
   cp .env.example .env.development
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start development server:
   ```bash
   npm run dev
   ```

## Notes

- The deployment only triggers on pushes to `main` or `master` branches
- Make sure your backend API is accessible from the deployed frontend
- Update the `PRODUCTION_API_URL` secret when your API URL changes
