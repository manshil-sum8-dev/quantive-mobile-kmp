package za.co.quantive.shared.config

/**
 * Feature flags for controlling feature availability
 * Allows enabling/disabling features without code changes
 */
data class FeatureFlags(
    val enableBiometricAuth: Boolean,
    val enableOfflineMode: Boolean,
    val enableAnalytics: Boolean,
    val enableNotifications: Boolean,
    val enableBudgetInsights: Boolean,
    val enableExpenseCategories: Boolean,
    val enableRecurringTransactions: Boolean,
    val enableDataExport: Boolean,
    val enableDarkMode: Boolean
) {
    companion object {
        /**
         * All features enabled - for development and staging
         */
        fun allEnabled(): FeatureFlags {
            return FeatureFlags(
                enableBiometricAuth = true,
                enableOfflineMode = false, // Not implemented yet
                enableAnalytics = true,
                enableNotifications = true,
                enableBudgetInsights = true,
                enableExpenseCategories = true,
                enableRecurringTransactions = true,
                enableDataExport = true,
                enableDarkMode = true
            )
        }

        /**
         * Production features - conservative defaults
         */
        fun production(): FeatureFlags {
            return FeatureFlags(
                enableBiometricAuth = true,
                enableOfflineMode = false, // Not implemented yet
                enableAnalytics = true,
                enableNotifications = true,
                enableBudgetInsights = false, // Enable after testing
                enableExpenseCategories = true,
                enableRecurringTransactions = false, // Enable after testing
                enableDataExport = false, // Enable after testing
                enableDarkMode = true
            )
        }

        /**
         * Minimal features - for testing core functionality
         */
        fun minimal(): FeatureFlags {
            return FeatureFlags(
                enableBiometricAuth = false,
                enableOfflineMode = false,
                enableAnalytics = false,
                enableNotifications = false,
                enableBudgetInsights = false,
                enableExpenseCategories = true,
                enableRecurringTransactions = false,
                enableDataExport = false,
                enableDarkMode = false
            )
        }
    }
}
