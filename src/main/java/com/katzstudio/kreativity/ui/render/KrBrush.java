package com.katzstudio.kreativity.ui.render;

import lombok.EqualsAndHashCode;

/**
 * The {@link KrBrush} class describes the fill pattern of shapes rendered by the {@link KrRenderer}
 * <p>
 * Due to the nature of the {@link KrRenderer} implementation, the {@link KrBrush} cannot offer any interface.
 * Instead, subclasses offer the data necessary for the renderer to perform the actual rendering.
 */
@EqualsAndHashCode
public class KrBrush {
}
