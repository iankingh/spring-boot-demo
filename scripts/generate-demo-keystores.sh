#!/bin/sh

set -eu

usage() {
  echo "Usage: OAUTH_DEMO_KEYSTORE_PASSWORD=... HTTPS_DEMO_KEYSTORE_PASSWORD=... $0 OUTPUT_DIRECTORY" >&2
  exit 2
}

[ "$#" -eq 1 ] || usage
: "${OAUTH_DEMO_KEYSTORE_PASSWORD:?Set OAUTH_DEMO_KEYSTORE_PASSWORD for the local OAuth fixture}"
: "${HTTPS_DEMO_KEYSTORE_PASSWORD:?Set HTTPS_DEMO_KEYSTORE_PASSWORD for the local HTTPS fixture}"

if ! command -v keytool >/dev/null 2>&1; then
  echo "keytool was not found; install a JDK before generating demo key stores." >&2
  exit 1
fi

output_directory=$1
oauth_key_store="$output_directory/oauth2.jks"
https_key_store="$output_directory/server.keystore"

if [ -e "$oauth_key_store" ] || [ -e "$https_key_store" ]; then
  echo "Refusing to overwrite an existing key store in: $output_directory" >&2
  exit 1
fi

umask 077
mkdir -p "$output_directory"

keytool -genkeypair -noprompt \
  -alias oauth2 \
  -dname "CN=local-oauth-demo, OU=Test Fixtures, O=spring-boot-demo, C=XX" \
  -keyalg RSA \
  -keysize 2048 \
  -sigalg SHA256withRSA \
  -validity 365 \
  -storetype JKS \
  -keystore "$oauth_key_store" \
  -storepass "$OAUTH_DEMO_KEYSTORE_PASSWORD" \
  -keypass "$OAUTH_DEMO_KEYSTORE_PASSWORD"

keytool -genkeypair -noprompt \
  -alias tomcat \
  -dname "CN=localhost, OU=Test Fixtures, O=spring-boot-demo, C=XX" \
  -ext "SAN=dns:localhost,ip:127.0.0.1" \
  -keyalg RSA \
  -keysize 2048 \
  -sigalg SHA256withRSA \
  -validity 365 \
  -storetype JKS \
  -keystore "$https_key_store" \
  -storepass "$HTTPS_DEMO_KEYSTORE_PASSWORD" \
  -keypass "$HTTPS_DEMO_KEYSTORE_PASSWORD"

keytool -list -alias oauth2 -keystore "$oauth_key_store" \
  -storepass "$OAUTH_DEMO_KEYSTORE_PASSWORD" >/dev/null
keytool -list -alias tomcat -keystore "$https_key_store" \
  -storepass "$HTTPS_DEMO_KEYSTORE_PASSWORD" >/dev/null

echo "Generated local-only demo key stores in: $output_directory"

