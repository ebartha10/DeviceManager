FROM nginx:alpine

# Copy OpenAPI spec and Swagger UI HTML
COPY openapi.yaml /usr/share/nginx/html/openapi.yaml
COPY index.html /usr/share/nginx/html/index.html

# Configure nginx to serve the files
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    \
    # Exact match for YAML file - must come before catch-all \
    location = /openapi.yaml { \
        default_type application/yaml; \
        add_header Content-Type "application/yaml; charset=utf-8"; \
        add_header Access-Control-Allow-Origin *; \
        add_header Access-Control-Allow-Methods "GET, OPTIONS"; \
    } \
    \
    # Root path serves index.html \
    location = / { \
        try_files /index.html =404; \
    } \
    \
    # Catch-all for other paths (e.g., /index.html) \
    location / { \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

