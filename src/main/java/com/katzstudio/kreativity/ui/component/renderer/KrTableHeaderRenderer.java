package com.katzstudio.kreativity.ui.component.renderer;

import com.katzstudio.kreativity.ui.component.KrTableView;
import com.katzstudio.kreativity.ui.component.KrWidget;

/**
 * Renderer for table headers.
 */
public interface KrTableHeaderRenderer {
    KrWidget getComponent(int column, KrTableView.KrTableColumnModel columnModel);
}
