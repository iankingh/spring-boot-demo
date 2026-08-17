# Security notes

## Demo key material

This repository intentionally keeps two historical key stores so the Java 8 examples remain reproducible:

- `demo-https/src/main/resources/server.keystore` provides the self-signed HTTPS key used by `demo-https`.
- `demo-oauth/oauth-authorization-server/src/main/resources/oauth2.jks` provides the JWT signing key used by the OAuth authorization-server demo.

Both files contain private keys and expired self-signed certificates. They and their publicly visible demo passwords are **local test fixtures only**. Never deploy them, import them into a trusted key store, use them as a certificate authority, or use their keys or passwords for any real account or environment.

For local experiments, generate fresh fixtures outside the source tree:

```bash
OAUTH_DEMO_KEYSTORE_PASSWORD='choose-a-local-password' \
HTTPS_DEMO_KEYSTORE_PASSWORD='choose-another-local-password' \
./scripts/generate-demo-keystores.sh /tmp/spring-boot-demo-keystores
```

The script refuses to overwrite existing files. If a generated fixture is used by a demo, configure that local run with the matching password. Production deployments must instead load independently managed, rotated keys from an appropriate secret store and use certificates issued for the deployed host.

