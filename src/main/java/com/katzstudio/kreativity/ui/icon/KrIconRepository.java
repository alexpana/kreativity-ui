package com.katzstudio.kreativity.ui.icon;

import com.katzstudio.kreativity.ui.KrFontAwesomeGlyph;

import java.util.HashMap;
import java.util.Map;

/**
 * The icon repository creates and caches {@link KrIcon}s
 */
public class KrIconRepository {

    private static final int ICON_SIZE = 14;

    private final Map<KrFontAwesomeGlyph, KrIcon> fontAwesomeIcons = new HashMap<>();

    public KrIcon getIcon(KrFontAwesomeGlyph fontAwesomeGlyph) {
        if (!fontAwesomeIcons.containsKey(fontAwesomeGlyph)) {
            KrIcon icon = new KrFontAwesomeIcon(fontAwesomeGlyph);
            fontAwesomeIcons.put(fontAwesomeGlyph, icon);
            return icon;
        }
        return fontAwesomeIcons.get(fontAwesomeGlyph);
    }
}
