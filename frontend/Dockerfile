FROM node:20-alpine AS development-dependencies-env
COPY . /app
WORKDIR /app
RUN npm ci

FROM node:20-alpine AS production-dependencies-env
COPY ./package.json package-lock.json /app/
WORKDIR /app
RUN npm ci --omit=dev

FROM node:20-alpine AS build-env
COPY . /app/
COPY --from=development-dependencies-env /app/node_modules /app/node_modules
WORKDIR /app
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

FROM node:20-alpine
# Create a non-root user for better security
RUN addgroup -g 1001 -S nodejs && \
    adduser -S nodejs -u 1001

COPY ./package.json package-lock.json /app/
COPY --from=production-dependencies-env /app/node_modules /app/node_modules
COPY --from=build-env /app/build /app/build
WORKDIR /app

# Change ownership to non-root user
RUN chown -R nodejs:nodejs /app

USER nodejs

# Expose port 3000 (React Router Serve default)
EXPOSE 3000

# Set environment variable for port (can be overridden)
ENV PORT=3000
ENV NODE_ENV=production

CMD ["npm", "run", "start"]