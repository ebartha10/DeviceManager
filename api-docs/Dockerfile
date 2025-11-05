FROM swaggerapi/swagger-ui:latest

COPY openapi.yaml /tmp/openapi.yaml

# Configure Swagger UI to use the file
ENV SWAGGER_JSON=/tmp/openapi.yaml

# Set BASE_URL so Swagger UI serves at /api-docs
ENV BASE_URL=/api-docs

EXPOSE 8080

