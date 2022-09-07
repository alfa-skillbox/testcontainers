## Keycloak
### Процедура запуска докер контейнеров
keycloak_up.sh - билдит и запускает контейнер keycloak
keycloak_down.sh - стопает сервис и удаляет контейнер keycloak и его volumes

### Получение токена для обращения к сервису
curl --location --request POST 'http://localhost:8888/auth/realms/alfa-skillbox/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=modules-tests-client' \
--data-urlencode 'client_secret=kFdfYNqWFPtb9UALCXNc4yhsS2zrX0XV' \
--data-urlencode 'grant_type=client_credentials'

Порт 8888 берется из настроек docker-compose.yml

### Экспортировать конфигурацию запущенного контейнера кейклока
docker exec -it keycloak-local /opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.realmName=alfa-skillbox \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/alfa-skillbox_realm_config.json

##Postgresql
### Процедура запуска докер контейнеров
postgre_up.sh - билдит и запускает контейнер postgres и его админку adminer
postgre_down.sh - стопает сервис и удаляет контейнер postgres и его админку adminer
postgre_down_and_clean_db.sh - стопает сервис и удаляет контейнер postgres и его volumes и его админку adminer