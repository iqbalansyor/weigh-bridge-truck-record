package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    testTag: String,
    onFocusChange: (FocusState) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    errorMessage: String? = null,
    placeholder: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.LightGray, shape = MaterialTheme.shapes.medium)
    ) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            modifier = Modifier
                .testTag(testTag)
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },
            readOnly = readOnly,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(placeholder) },
            label = { Text(hint) },
            keyboardOptions = keyboardOptions,
            isError = !errorMessage.isNullOrBlank(),
        )
        if (!errorMessage.isNullOrBlank()) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 1.dp,
                    bottom = 1.dp
                ),
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    TransparentHintTextField(
        text = "Text",
        hint = "hint",
        onValueChange = {},
        singleLine = false,
        testTag = "",
        onFocusChange = {})
}