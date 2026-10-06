# Security notes

## Configuration secrets and database transport

Tracked sample configuration and README snippets use Spring property placeholders
such as `${DEMO_ORM_JPA_SRC_MAIN_RESOURCES_PASSWORD}` instead of embedded passwords
or provider client secrets. Set every placeholder referenced by the module you run
in its process environment or an untracked local configuration file. There are no
fallback credential values: a missing value must be supplied before the relevant
service can start. Keep real credentials out of tracked files and rotate any
credential that may have been exposed previously.

MySQL JDBC examples require TLS and server identity verification
(`sslMode=VERIFY_IDENTITY`). Configure the database certificate and the JVM trust
store so the certificate chain is trusted and its subject matches the JDBC host;
the local `127.0.0.1` and `localhost` examples therefore need matching local
certificates. Do not weaken this setting for non-local or sensitive connections.
RabbitMQ's `guest` account is a broker-provided sample identity only and must
remain confined to a local broker; set its password via the module's required
environment placeholder.
Credentials instantiated directly by integration tests (for example, local
code-generator database tests and OAuth test users) are test fixtures only; do
not reuse them outside an isolated local test environment.

## Demo key material

This repository intentionally keeps two historical key stores so the Java 8 examples remain reproducible:

- `demo-https/src/main/resources/server.keystore` provides the self-signed HTTPS key used by `demo-https`.
- `demo-oauth/oauth-authorization-server/src/main/resources/oauth2.jks` provides the JWT signing key used by the OAuth authorization-server demo.

Both files contain private keys and expired self-signed certificates. They and their publicly visible demo passwords are **local test fixtures only**. Never deploy them, import them into a trusted key store, use them as a certificate authority, or use their keys or passwords for any real account or environment.

For local experiments, generate fresh fixtures outside the source tree:

```bash
OAUTH_DEMO_KEYSTORE_PASSWORD='choose-a-local-password' \
HTTPS_DEMO_KEYSTORE_PASSWORD='choose-another-local-password' \
./scripts/generate-demo-keystores.sh "$HOME/spring-boot-demo-keystores"
```

The script refuses to overwrite existing files. If a generated fixture is used by a demo, configure that local run with the matching password. Production deployments must instead load independently managed, rotated keys from an appropriate secret store and use certificates issued for the deployed host.
