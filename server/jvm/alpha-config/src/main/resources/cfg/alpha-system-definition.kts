/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide system definition config for multi-pro-code-test.
 *
 * Modification History
 */
systemDefinition {
    global {
        item(name = "ADMIN_PERMISSION_ENTITY_TABLE", value = "COUNTERPARTY")
        item(name = "ADMIN_PERMISSION_ENTITY_FIELD", value = "COUNTERPARTY_ID")

        item(name = "SFTP_USERNAME", value = "JohnDoe")
        item(name = "SFTP_PASSWORD", value = "Password11")

        item(name = "SYSTEM_DEFAULT_USER_NAME", value = "Genesis System")
        item(name = "SYSTEM_DEFAULT_EMAIL", value = "notifications@genesis.global")
        item(name = "EMAIL_SMTP_HOST", value = "INSERT YOUR HOST")
        item(name = "EMAIL_SMTP_PORT", value = "587")
        item(name = "EMAIL_SMTP_USER", value = "INSERT YOUR USER")
        item(name = "EMAIL_SMTP_PW", value = "INSERT YOUR PASSWORD")
        item(name = "EMAIL_SMTP_PROTOCOL", value = "SMTP_TLS")
    }

    systems {

    }

}