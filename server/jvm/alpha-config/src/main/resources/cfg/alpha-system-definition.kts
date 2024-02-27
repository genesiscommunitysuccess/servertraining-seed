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
        item(name = "BASE_DIR_FILES", value = "/home/alpha/run/runtime/GUI_TEST/files")

        item(name = "SFTP_PATH", value = "sftp:22/")
        item(name = "SFTP_USERNAME", value = "JohnDoe")
        item(name = "SFTP_PASSWORD", value = "Password11")
        item(name = "SFTP_DIRECTORY", value = "folder-inside-sftp")
        item(name = "SFTP_FILE", value = "from.txt")
    }

    systems {

    }

}