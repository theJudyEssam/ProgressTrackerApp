package com.example.progress_tracker.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


val Pen: ImageVector
    get() {
        if (_Pen != null) return _Pen!!

        _Pen = ImageVector.Builder(
            name = "Pen",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveToRelative(13.498f, 0.795f)
                lineToRelative(0.149f, -0.149f)
                arcToRelative(1.207f, 1.207f, 0f, true, true, 1.707f, 1.708f)
                lineToRelative(-0.149f, 0.148f)
                arcToRelative(1.5f, 1.5f, 0f, false, true, -0.059f, 2.059f)
                lineTo(4.854f, 14.854f)
                arcToRelative(0.5f, 0.5f, 0f, false, true, -0.233f, 0.131f)
                lineToRelative(-4f, 1f)
                arcToRelative(0.5f, 0.5f, 0f, false, true, -0.606f, -0.606f)
                lineToRelative(1f, -4f)
                arcToRelative(0.5f, 0.5f, 0f, false, true, 0.131f, -0.232f)
                lineToRelative(9.642f, -9.642f)
                arcToRelative(0.5f, 0.5f, 0f, false, false, -0.642f, 0.056f)
                lineTo(6.854f, 4.854f)
                arcToRelative(0.5f, 0.5f, 0f, true, true, -0.708f, -0.708f)
                lineTo(9.44f, 0.854f)
                arcTo(1.5f, 1.5f, 0f, false, true, 11.5f, 0.796f)
                arcToRelative(1.5f, 1.5f, 0f, false, true, 1.998f, -0.001f)
                moveToRelative(-0.644f, 0.766f)
                arcToRelative(0.5f, 0.5f, 0f, false, false, -0.707f, 0f)
                lineTo(1.95f, 11.756f)
                lineToRelative(-0.764f, 3.057f)
                lineToRelative(3.057f, -0.764f)
                lineTo(14.44f, 3.854f)
                arcToRelative(0.5f, 0.5f, 0f, false, false, 0f, -0.708f)
                close()
            }
        }.build()

        return _Pen!!
    }

private var _Pen: ImageVector? = null

