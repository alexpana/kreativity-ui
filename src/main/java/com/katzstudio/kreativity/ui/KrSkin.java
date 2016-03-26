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
import com.google.common.collect.ImmutableMap;
import com.katzstudio.kreativity.ui.style.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.*;

/**
 * Kreativity Skin
 */
public class KrSkin {

    private static final KrSkin INSTANCE = new KrSkin();

    private final ImmutableMap<ColorKey, Color> colors = ImmutableMap.<ColorKey, Color>builder()
            .put(BACKGROUND_LIGHT, new Color(0x424242ff))
            .put(BACKGROUND_DARK, new Color(0x353535ff))
            .put(FOREGROUND, new Color(0xDDDDDDFF))
            .put(SELECTION_BACKGROUND, new Color(0x38466bff))
            .build();

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

    @Getter @Setter private KrWidgetStyle listViewStyle;

    @Getter private Texture skinTexture;

    private KrSkin() {
    }

    public static KrSkin instance() {
        return INSTANCE;
    }

    public void install() {
        loadDefault();
    }

    public Drawable getDrawable(String name) {
        return drawablePatches.get(name);
    }

    private void loadDefault() {
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
        widgetStyle.background = KrToolkit.getDrawable(KrColor.TRANSPARENT);
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
        splitPanelStyle.splitterBackground = KrToolkit.getDrawable(getColor(BACKGROUND_LIGHT));
        splitPanelStyle.splitterGrip = getDrawable("split_panel.splitter_grip");

        listViewStyle = new KrWidgetStyle(widgetStyle);
    }

    private Rectangle jsonArrayToRectangle(JsonValue jsonValue) {
        return new Rectangle(jsonValue.get(0).asInt(), jsonValue.get(1).asInt(), jsonValue.get(2).asInt(), jsonValue.get(3).asInt());
    }

    public Color getColor(ColorKey key) {
        return colors.get(key);
    }

    public enum ColorKey {
        BACKGROUND_LIGHT,
        BACKGROUND_DARK,
        FOREGROUND,
        SELECTION_BACKGROUND,
    }
}
