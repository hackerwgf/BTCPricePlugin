package com.danielstudio.idea.plugin.btcprice

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class PriceWidgetFactory : StatusBarWidgetFactory {

    override fun getId() = "PriceWidgetFactory"

    override fun getDisplayName() = "BTC Price"

    override fun isAvailable(project: Project) = true

    override fun createWidget(project: Project): StatusBarWidget {
        return PriceWidget()
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar) = true
}