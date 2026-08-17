# spring-boot-demo-oauth

> [!WARNING]
> `oauth-authorization-server/src/main/resources/oauth2.jks` 包含 JWT 签名私钥和已过期的自签名证书，只是为了保留历史 demo 的本机测试 fixture。源码中的密码是公开的 demo 值；不得用于正式 token、真实账号或任何部署环境。需要新本机 key store 时，请依照根目录的 [`SECURITY.md`](../SECURITY.md) 生成，不要覆盖 tracked fixture。

此聚合模组包含 OAuth authorization server 与 resource server。authorization server 的完整示例与测试说明见 [`oauth-authorization-server/README.adoc`](oauth-authorization-server/README.adoc)。
