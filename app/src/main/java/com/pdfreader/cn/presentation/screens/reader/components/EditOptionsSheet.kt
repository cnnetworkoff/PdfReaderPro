package com.pdfreader.cn.presentation.screens.reader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.BorderColor
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pdfreader.cn.R

private val EditBlue = Color(0xFF64B5F6)
private val EditTeal = Color(0xFF4DB6AC)
private val EditPurple = Color(0xFF9575CD)
private val EditAmber = Color(0xFFFFB74D)
private val EditPink = Color(0xFFF48FB1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOptionsSheet(
    isHighlightModeActive: Boolean,
    isTextInsertModeActive: Boolean,
    isDrawModeActive: Boolean,
    isStampModeActive: Boolean,
    hasUnsavedEdits: Boolean,
    onHighlightClick: () -> Unit,
    onTextInsertClick: () -> Unit,
    onDrawClick: () -> Unit,
    onStampClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onDoneClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EditSectionLabel(stringResource(R.string.edit_tools))

            EditOptionRow(
                icon = Icons.Rounded.BorderColor,
                title = stringResource(R.string.highlight_text),
                subtitle = if (isHighlightModeActive) stringResource(R.string.edit_mode_active) else stringResource(R.string.highlight_text_desc),
                accentColor = EditBlue,
                onClick = {
                    onDismiss()
                    onHighlightClick()
                }
            )

            EditOptionRow(
                icon = Icons.Rounded.TextFields,
                title = stringResource(R.string.insert_text),
                subtitle = if (isTextInsertModeActive) stringResource(R.string.edit_mode_active) else stringResource(R.string.insert_text_desc),
                accentColor = EditPurple,
                onClick = {
                    onDismiss()
                    onTextInsertClick()
                }
            )

            EditOptionRow(
                icon = Icons.Rounded.Draw,
                title = stringResource(R.string.draw_annotation),
                subtitle = if (isDrawModeActive) stringResource(R.string.edit_mode_active) else stringResource(R.string.draw_annotation_desc),
                accentColor = EditTeal,
                onClick = {
                    onDismiss()
                    onDrawClick()
                }
            )

            EditOptionRow(
                icon = Icons.Rounded.Brush,
                title = stringResource(R.string.add_stamp),
                subtitle = if (isStampModeActive) stringResource(R.string.edit_mode_active) else stringResource(R.string.add_stamp_desc),
                accentColor = EditPink,
                onClick = {
                    onDismiss()
                    onStampClick()
                }
            )

            Spacer(modifier = Modifier.size(4.dp))
            EditSectionLabel(stringResource(R.string.edit_actions))

            EditOptionRow(
                icon = Icons.AutoMirrored.Rounded.Undo,
                title = stringResource(R.string.undo),
                subtitle = stringResource(R.string.undo_last_edit),
                accentColor = EditAmber,
                onClick = {
                    onDismiss()
                    onUndoClick()
                }
            )

            EditOptionRow(
                icon = Icons.Rounded.Refresh,
                title = stringResource(R.string.redo),
                subtitle = stringResource(R.string.redo_last_edit),
                accentColor = EditAmber,
                onClick = {
                    onDismiss()
                    onRedoClick()
                }
            )

            EditOptionRow(
                icon = if (hasUnsavedEdits) Icons.Rounded.EditNote else Icons.Rounded.DoneAll,
                title = stringResource(R.string.done_editing),
                subtitle = if (hasUnsavedEdits) stringResource(R.string.done_editing_desc) else stringResource(R.string.edit_mode_off_desc),
                accentColor = EditBlue,
                onClick = {
                    onDismiss()
                    onDoneClick()
                }
            )
        }
    }
}

@Composable
private fun EditSectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun EditOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
