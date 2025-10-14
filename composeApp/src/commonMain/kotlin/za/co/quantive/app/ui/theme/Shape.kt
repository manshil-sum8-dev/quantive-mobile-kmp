package za.co.quantive.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Shapes
 * Features extra-rounded corners for a playful, friendly appearance
 */
val ExpressiveShapes = Shapes(
    // Extra small components (chips, tags)
    extraSmall = RoundedCornerShape(12.dp),

    // Small components (buttons, small cards)
    small = RoundedCornerShape(16.dp),

    // Medium components (cards, dialogs)
    medium = RoundedCornerShape(20.dp),

    // Large components (sheets, large cards)
    large = RoundedCornerShape(28.dp),

    // Extra large components (full-screen dialogs)
    extraLarge = RoundedCornerShape(32.dp)
)
