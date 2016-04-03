package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.katzstudio.kreativity.ui.style.KrButtonGroupStyle;
import com.katzstudio.kreativity.ui.style.KrButtonStyle;
import com.katzstudio.kreativity.ui.style.KrCheckboxStyle;
import com.katzstudio.kreativity.ui.style.KrItemViewStyle;
import com.katzstudio.kreativity.ui.style.KrLabelStyle;
import com.katzstudio.kreativity.ui.style.KrPanelStyle;
import com.katzstudio.kreativity.ui.style.KrScrollBarStyle;
import com.katzstudio.kreativity.ui.style.KrSplitPanelStyle;
import com.katzstudio.kreativity.ui.style.KrTextFieldStyle;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_DARK;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_LIGHT;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BORDER;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.FOREGROUND;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SELECTION_BACKGROUND;

/**
 * Kreativity Skin
 */
public class KrSkin {

    private final Map<ColorKey, Color> colors = new HashMap<>();

    {
        colors.put(BACKGROUND, new Color(0x4C4C4Cff));
        colors.put(BACKGROUND_LIGHT, new Color(0x424242ff));
        colors.put(BACKGROUND_DARK, new Color(0x353535ff));
        colors.put(BORDER, new Color(0x323232ff));
        colors.put(FOREGROUND, new Color(0xDDDDDDFF));
        colors.put(SELECTION_BACKGROUND, new Color(0x4b6eafff));
    }

    private final Map<String, Drawable> drawablePatches = new HashMap<>();

    @Getter private BitmapFont fontAwesome;

    @Getter private BitmapFont defaultFont;

    @Getter private BitmapFont defaultFontBold;

    @Getter @Setter private KrWidgetStyle widgetStyle;

    @Getter @Setter private KrPanelStyle panelStyle;

    @Getter @Setter private KrLabelStyle labelStyle;

    @Getter @Setter private KrCheckboxStyle checkboxStyle;

    @Getter @Setter private KrButtonStyle buttonStyle;

    @Getter @Setter private KrButtonStyle buttonGroupFirstButtonStyle;

    @Getter @Setter private KrButtonStyle buttonGroupMiddleButtonStyle;

    @Getter @Setter private KrButtonStyle buttonGroupLastButtonStyle;

    @Getter @Setter private KrButtonGroupStyle buttonGroupStyle;

    @Getter @Setter private KrTextFieldStyle textFieldStyle;

    @Getter @Setter private KrTextFieldStyle spinnerStyle;

    @Getter @Setter private KrScrollBarStyle verticalScrollBarStyle;

    @Getter @Setter private KrScrollBarStyle horizontalScrollBarStyle;

    @Getter @Setter private KrSplitPanelStyle splitPanelStyle;

    @Getter @Setter private KrItemViewStyle listViewStyle;

    @Getter @Setter private KrItemViewStyle tableViewStyle;

    @Getter private Texture skinTexture;

    KrSkin(KrToolkit toolkit) {
        loadDefault(toolkit);
    }

    public Drawable getDrawable(String name) {
        return drawablePatches.get(name);
    }

    void loadDefault(KrToolkit toolkit) {
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonSkin = jsonReader.parse(Gdx.files.classpath("ui/skin.json"));

        String textureName = jsonSkin.get("texture").asString();
        skinTexture = new Texture(Gdx.files.classpath("ui/" + textureName));

        String defaultFontPath = jsonSkin.get("fonts").get("default").asString();
        defaultFont = new BitmapFont(Gdx.files.classpath("ui/" + defaultFontPath));

        String boldFontPath = jsonSkin.get("fonts").get("bold").asString();
        defaultFontBold = new BitmapFont(Gdx.files.classpath("ui/" + boldFontPath));

        String fontAwesomePath = jsonSkin.get("fonts").get("fontawesome").asString();
        fontAwesome = new BitmapFont(Gdx.files.classpath("ui/" + fontAwesomePath));

        JsonValue patches = jsonSkin.get("patches");
        for (JsonValue patch : patches) {
            String patchName = patch.name;
            Rectangle regionRect = jsonArrayToRectangle(patch.get("region"));
            Rectangle regionSplit = jsonArrayToRectangle(patch.get("split"));

            TextureRegion textureRegion = new TextureRegion(skinTexture, (int) regionRect.x, (int) regionRect.y, (int) regionRect.width, (int) regionRect.height);
            NinePatch ninePatch = new NinePatch(textureRegion, (int) regionSplit.x, (int) regionSplit.y, (int) regionSplit.width, (int) regionSplit.height);
            NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(ninePatch);
            ninePatchDrawable.setName(patchName);

            drawablePatches.put(patchName, ninePatchDrawable);
        }

        widgetStyle = new KrWidgetStyle();
        widgetStyle.background = toolkit.getDrawable(getColor(BACKGROUND));
        widgetStyle.foregroundColor = getColor(FOREGROUND);
        widgetStyle.font = getDefaultFont();
        widgetStyle.textShadowOffset = new Vector2(0, 0);
        widgetStyle.textShadowColor = Color.BLACK;
        widgetStyle.icon = null;
        widgetStyle.selectionColor = KrColor.rgb(0x38466b);
        widgetStyle.padding = new KrPadding(0);
        widgetStyle.cursor = KrCursor.ARROW;

        panelStyle = new KrPanelStyle(widgetStyle);

        labelStyle = new KrLabelStyle(widgetStyle);
        labelStyle.foregroundColor = KrColor.rgb(0xffffff);
        labelStyle.padding = new KrPadding(4);

        buttonStyle = new KrButtonStyle(widgetStyle);
        buttonStyle.backgroundNormal = drawablePatches.get("button.background_normal");
        buttonStyle.backgroundHovered = drawablePatches.get("button.background_hovered");
        buttonStyle.backgroundArmed = drawablePatches.get("button.background_armed");
        buttonStyle.textShadowOffset = new Vector2(0, 1);
        buttonStyle.textShadowColor = new Color(0x00000060);

        checkboxStyle = new KrCheckboxStyle(widgetStyle);
        checkboxStyle.checkboxBackground = drawablePatches.get("checkbox.background");
        checkboxStyle.mark = drawablePatches.get("checkbox.mark");

        textFieldStyle = new KrTextFieldStyle(widgetStyle);
        textFieldStyle.backgroundNormal = drawablePatches.get("textfield.background_normal");
        textFieldStyle.backgroundHovered = drawablePatches.get("textfield.background_hovered");
        textFieldStyle.backgroundFocused = drawablePatches.get("textfield.background_focused");
        textFieldStyle.caretColor = getColor(FOREGROUND);
        textFieldStyle.padding = new KrPadding(1, 4, 4, 4);
        textFieldStyle.cursor = KrCursor.IBEAM;

        spinnerStyle = new KrTextFieldStyle(textFieldStyle);
        spinnerStyle.backgroundNormal = drawablePatches.get("spinner.background_normal");
        spinnerStyle.backgroundHovered = drawablePatches.get("spinner.background_hovered");
        spinnerStyle.backgroundFocused = drawablePatches.get("spinner.background_focused");
        spinnerStyle.padding = new KrPadding(1, 17, 4, 4);

        verticalScrollBarStyle = new KrScrollBarStyle(widgetStyle);
        verticalScrollBarStyle.track = drawablePatches.get("scrollbar.vertical.track");
        verticalScrollBarStyle.thumb = drawablePatches.get("scrollbar.vertical.thumb");
        verticalScrollBarStyle.size = 5;
        verticalScrollBarStyle.splitterCursor = KrCursor.VERTICAL_RESIZE;

        horizontalScrollBarStyle = new KrScrollBarStyle(widgetStyle);
        horizontalScrollBarStyle.track = drawablePatches.get("scrollbar.horizontal.track");
        horizontalScrollBarStyle.thumb = drawablePatches.get("scrollbar.horizontal.thumb");
        horizontalScrollBarStyle.size = 5;
        horizontalScrollBarStyle.splitterCursor = KrCursor.HORIZONTAL_RESIZE;

        buttonGroupFirstButtonStyle = new KrButtonStyle(buttonStyle);
        buttonGroupFirstButtonStyle.backgroundNormal = drawablePatches.get("button_group_first.background_normal");
        buttonGroupFirstButtonStyle.backgroundHovered = drawablePatches.get("button_group_first.background_hovered");
        buttonGroupFirstButtonStyle.backgroundArmed = drawablePatches.get("button_group_first.background_armed");

        buttonGroupMiddleButtonStyle = new KrButtonStyle(buttonStyle);
        buttonGroupMiddleButtonStyle.backgroundNormal = drawablePatches.get("button_group_middle.background_normal");
        buttonGroupMiddleButtonStyle.backgroundHovered = drawablePatches.get("button_group_middle.background_hovered");
        buttonGroupMiddleButtonStyle.backgroundArmed = drawablePatches.get("button_group_middle.background_armed");

        buttonGroupLastButtonStyle = new KrButtonStyle(buttonStyle);
        buttonGroupLastButtonStyle.backgroundNormal = drawablePatches.get("button_group_last.background_normal");
        buttonGroupLastButtonStyle.backgroundHovered = drawablePatches.get("button_group_last.background_hovered");
        buttonGroupLastButtonStyle.backgroundArmed = drawablePatches.get("button_group_last.background_armed");

        buttonGroupStyle = new KrButtonGroupStyle(widgetStyle);
        buttonGroupStyle.singleButtonStyle = buttonStyle;
        buttonGroupStyle.firstButtonStyle = buttonGroupFirstButtonStyle;
        buttonGroupStyle.middleButtonStyle = buttonGroupMiddleButtonStyle;
        buttonGroupStyle.lastButtonStyle = buttonGroupLastButtonStyle;

        splitPanelStyle = new KrSplitPanelStyle(widgetStyle);
        splitPanelStyle.splitterBackground = toolkit.getDrawable(getColor(BACKGROUND_LIGHT));
        splitPanelStyle.splitterGrip = getDrawable("split_panel.splitter_grip");

        listViewStyle = new KrItemViewStyle(widgetStyle);
        listViewStyle.gridColor = getColor(FOREGROUND);
        listViewStyle.gridVisible = false;

        tableViewStyle = new KrItemViewStyle(widgetStyle);
        tableViewStyle.gridColor = getColor(BACKGROUND_LIGHT);
        tableViewStyle.gridVisible = true;
    }

    private Rectangle jsonArrayToRectangle(JsonValue jsonValue) {
        return new Rectangle(jsonValue.get(0).asInt(), jsonValue.get(1).asInt(), jsonValue.get(2).asInt(), jsonValue.get(3).asInt());
    }

    public Color getColor(ColorKey key) {
        return colors.get(key);
    }

    public enum ColorKey {
        BACKGROUND,
        BACKGROUND_LIGHT,
        BACKGROUND_DARK,
        FOREGROUND,
        SELECTION_BACKGROUND,
        BORDER,
    }
}
