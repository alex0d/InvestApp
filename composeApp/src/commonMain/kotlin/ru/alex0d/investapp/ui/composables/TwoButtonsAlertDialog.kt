package ru.alex0d.investapp.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.confirm
import investapp.composeapp.generated.resources.dismiss
import org.jetbrains.compose.resources.stringResource

@Composable
fun TwoButtonsAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = null)
        },
        title = {
            Text(
                text = dialogTitle,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = stringResource(Res.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(Res.string.dismiss))
            }
        }
    )
}