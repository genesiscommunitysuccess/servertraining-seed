package scripts

notify {
    gateways {
        teams("teams") {
            url = "https://netorg209792.webhook.office.com/webhookb2/83334165-d08b-42a2-93f5-944a9743cec6@5f4c432a-2ed3-44db-a2ab-ffb3b97da200/IncomingWebhook/ae6dfea397ce4ee08318d9c83cb9a7d5/f5f24e8b-3138-404d-9bc0-16cc2bdad2c7"
        }
        email(id = "email") {
            smtpHost = systemDefinition["EMAIL_SMTP_HOST"].get()
            smtpPort = systemDefinition["EMAIL_SMTP_PORT"].get().toInt()
            smtpUser = systemDefinition["EMAIL_SMTP_USER"].get()
            smtpPw = systemDefinition["EMAIL_SMTP_PW"].get()
            smtpProtocol = TransportStrategy.SMTP_TLS
            systemDefaultUserName = systemDefinition["SYSTEM_DEFAULT_USER_NAME"].get()
            systemDefaultEmail = systemDefinition["SYSTEM_DEFAULT_EMAIL"].get()
            staticDistribution {
                to = listOf("guilherme.guerra@genesis.global")
            }
        }
    }
}