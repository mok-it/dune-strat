package hu.mokegyesulet.it.dunestrat.myiconpack.composehex.idea

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.String
import kotlin.collections.List as ____KtList
import kotlin.collections.Map as ____KtMap

public object LibrariesGroup

public val Libraries: LibrariesGroup
    get() = LibrariesGroup

public val LibrariesGroup.groupName: String
    get() = "libraries"

private var __AllIcons: ____KtList<ImageVector>? = null

public val LibrariesGroup.AllIcons: ____KtList<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf()
        return __AllIcons!!
    }

private var __AllIconsNamed: ____KtMap<String, ImageVector>? = null

public val LibrariesGroup.AllIconsNamed: ____KtMap<String, ImageVector>
    get() {
        if (__AllIconsNamed != null) {
            return __AllIconsNamed!!
        }
        __AllIconsNamed = mapOf()
        return __AllIconsNamed!!
    }
