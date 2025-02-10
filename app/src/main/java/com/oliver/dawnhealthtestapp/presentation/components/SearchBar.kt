package com.oliver.dawnhealthtestapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    input: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.secondary,
    hintColor: Color = MaterialTheme.colorScheme.tertiary,
    textStyle: TextStyle = TextStyle(
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
    ),
    onSearch: (String) -> Unit,
) {
    var text by remember { mutableStateOf(input) }
    var isHintDisplayed by remember { mutableStateOf(hint.isNotEmpty()) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = if (isHintDisplayed) hintColor else textColor,
                modifier = Modifier.padding(end = 8.dp)
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { inputText ->
                        text = inputText
                        onSearch(inputText)
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = textStyle.copy(color = textColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isHintDisplayed = !focusState.isFocused && text.isEmpty()
                        }
                )
                if (isHintDisplayed) {
                    Text(
                        text = hint,
                        style = textStyle.copy(color = hintColor),
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    SearchBar(
        hint = "Some hint",
        onSearch = {},
        input = ""
    )
}