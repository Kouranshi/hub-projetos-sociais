FROM quay.io/keycloak/keycloak:24.0

ENV KC_DB=postgres
ENV KC_DB_URL_HOST=DB_HOST
ENV KC_DB_URL_DATABASE=railway
ENV KC_DB_USERNAME=postgres
ENV KC_DB_PASSWORD=DB_PASSWORD
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin123

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--optimized"]