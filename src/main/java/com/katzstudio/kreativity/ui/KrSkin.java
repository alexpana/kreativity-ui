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
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrButtonGroup;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrComboBox;
import com.katzstudio.kreativity.ui.component.KrIconPanel;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrListView;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrScrollBar;
import com.katzstudio.kreativity.ui.component.KrSpinner;
import com.katzstudio.kreativity.ui.component.KrSplitPanel;
import com.katzstudio.kreativity.ui.component.KrTableView;
import com.katzstudio.kreativity.ui.component.KrTextField;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.style.KrButtonGroupStyle;
import com.katzstudio.kreativity.ui.style.KrButtonStyle;
import com.katzstudio.kreativity.ui.style.KrCheckboxStyle;
import com.katzstudio.kreativity.ui.style.KrComboBoxStyle;
import com.katzstudio.kreativity.ui.style.KrItemViewStyle;
import com.katzstudio.kreativity.ui.style.KrLabelStyle;
import com.katzstudio.kreativity.ui.style.KrPanelStyle;
import com.katzstudio.kreativity.ui.style.KrScrollBarStyle;
import com.katzstudio.kreativity.ui.style.KrSplitPanelStyle;
import com.katzstudio.kreativity.ui.style.KrTextFieldStyle;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_DARK;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_LIGHT;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BORDER;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.FOREGROUND;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SCROLLBAR_THUMB;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SELECTION_BACKGROUND;

/**
 * Kreativity Skin
 */
public class KrSkin {

    private final Map<ColorKey, Color> colors = new HashMap<>();

    private Map<Class<? extends KrWidget>, KrWidgetStyle> styles = new HashMap<>();

    {
        colors.put(BACKGROUND, new Color(0x4C4C4Cff));
        colors.put(BACKGROUND_LIGHT, new Color(0x424242ff));
        colors.put(BACKGROUND_DARK, new Color(0x353535ff));
        colors.put(BORDER, new Color(0x323232ff));
        colors.put(FOREGROUND, new Color(0xDDDDDDFF));
        colors.put(SELECTION_BACKGROUND, new Color(0x4b6eafff));
        colors.put(SCROLLBAR_THUMB, new Color(0x20202080));
    }

    private final Map<String, Drawable> drawablePatches = new HashMap<>();

    @Getter private BitmapFont fontAwesome;

    @Getter private BitmapFont defaultFont;

    @Getter private BitmapFont defaultFontBold;

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

        KrWidgetStyle widgetStyle = new KrWidgetStyle();
        widgetStyle.background = toolkit.getDrawable(getColor(BACKGROUND));
        widgetStyle.foregroundColor = getColor(FOREGROUND);
        widgetStyle.font = getDefaultFont();
        widgetStyle.textShadowOffset = new Vector2(0, 0);
        widgetStyle.textShadowColor = Color.BLACK;
        widgetStyle.icon = null;
        widgetStyle.selectionColor = KrColor.rgb(0x38466b);
        widgetStyle.padding = new KrPadding(0);
        widgetStyle.cursor = null;
        registerStyle(KrWidget.class, widgetStyle);

        KrPanelStyle panelStyle = new KrPanelStyle(widgetStyle);
        registerStyle(KrPanel.class, panelStyle);

        KrLabelStyle labelStyle = new KrLabelStyle(widgetStyle);
        labelStyle.foregroundColor = KrColor.rgb(0xffffff);
        labelStyle.background = toolkit.getDrawable(KrColor.TRANSPARENT);
        labelStyle.padding = new KrPadding(4);
        registerStyle(KrLabel.class, labelStyle);

        KrButtonStyle buttonStyle = new KrButtonStyle(widgetStyle);
        buttonStyle.backgroundNormal = drawablePatches.get("button.background_normal");
        buttonStyle.backgroundHovered = drawablePatches.get("button.background_hovered");
        buttonStyle.backgroundArmed = drawablePatches.get("button.background_armed");
        buttonStyle.textShadowOffset = new Vector2(0, 1);
        buttonStyle.textShadowColor = new Color(0x00000060);
        buttonStyle.padding = new KrPadding(5, 4);
        registerStyle(KrButton.class, buttonStyle);

        KrCheckboxStyle checkboxStyle = new KrCheckboxStyle(widgetStyle);
        checkboxStyle.checkboxBackground = drawablePatches.get("checkbox.background");
        checkboxStyle.mark = drawablePatches.get("checkbox.mark");
        checkboxStyle.padding = new KrPadding(2);
        registerStyle(KrCheckbox.class, checkboxStyle);

        KrTextFieldStyle textFieldStyle = new KrTextFieldStyle(widgetStyle);
        textFieldStyle.backgroundNormal = drawablePatches.get("textfield.background_normal");
        textFieldStyle.backgroundHovered = drawablePatches.get("textfield.background_hovered");
        textFieldStyle.backgroundFocused = drawablePatches.get("textfield.background_focused");
        textFieldStyle.caretColor = getColor(FOREGROUND);
        textFieldStyle.padding = new KrPadding(1, 4, 4, 4);
        textFieldStyle.cursor = KrCursor.IBEAM;
        registerStyle(KrTextField.class, textFieldStyle);

        KrTextFieldStyle spinnerStyle = new KrTextFieldStyle(textFieldStyle);
        spinnerStyle.backgroundNormal = drawablePatches.get("spinner.background_normal");
        spinnerStyle.backgroundHovered = drawablePatches.get("spinner.background_hovered");
        spinnerStyle.backgroundFocused = drawablePatches.get("spinner.background_focused");
        spinnerStyle.padding = new KrPadding(1, 17, 4, 4);
        registerStyle(KrSpinner.class, spinnerStyle);

        KrScrollBarStyle scrollBarStyle = new KrScrollBarStyle(widgetStyle);
        scrollBarStyle.track = toolkit.getDrawable(KrColor.TRANSPARENT); //drawablePatches.get("scrollbar.vertical.track");
        scrollBarStyle.foregroundColor = getColor(SCROLLBAR_THUMB);
        scrollBarStyle.thumb = drawablePatches.get("scrollbar.vertical.thumb");
        scrollBarStyle.size = 5;
        registerStyle(KrScrollBar.class, scrollBarStyle);

        KrButtonStyle buttonGroupFirstButtonStyle = new KrButtonStyle(buttonStyle);
        buttonGroupFirstButtonStyle.backgroundNormal = drawablePatches.get("button_group_first.background_normal");
        buttonGroupFirstButtonStyle.backgroundHovered = drawablePatches.get("button_group_first.background_hovered");
        buttonGroupFirstButtonStyle.backgroundArmed = drawablePatches.get("button_group_first.background_armed");

        KrButtonStyle buttonGroupMiddleButtonStyle = new KrButtonStyle(buttonStyle);
        buttonGroupMiddleButtonStyle.backgroundNormal = drawablePatches.get("button_group_middle.background_normal");
        buttonGroupMiddleButtonStyle.backgroundHovered = drawablePatches.get("button_group_middle.background_hovered");
        buttonGroupMiddleButtonStyle.backgroundArmed = drawablePatches.get("button_group_middle.background_armed");

        KrButtonStyle buttonGroupLastButtonStyle = new KrButtonStyle(buttonStyle);
        buttonGroupLastButtonStyle.backgroundNormal = drawablePatches.get("button_group_last.background_normal");
        buttonGroupLastButtonStyle.backgroundHovered = drawablePatches.get("button_group_last.background_hovered");
        buttonGroupLastButtonStyle.backgroundArmed = drawablePatches.get("button_group_last.background_armed");

        KrButtonGroupStyle buttonGroupStyle = new KrButtonGroupStyle(widgetStyle);
        buttonGroupStyle.singleButtonStyle = buttonStyle;
        buttonGroupStyle.firstButtonStyle = buttonGroupFirstButtonStyle;
        buttonGroupStyle.middleButtonStyle = buttonGroupMiddleButtonStyle;
        buttonGroupStyle.lastButtonStyle = buttonGroupLastButtonStyle;
        registerStyle(KrButtonGroup.class, buttonGroupStyle);

        KrSplitPanelStyle splitPanelStyle = new KrSplitPanelStyle(widgetStyle);
        splitPanelStyle.splitterBackground = toolkit.getDrawable(getColor(BACKGROUND_LIGHT));
        splitPanelStyle.splitterGrip = getDrawable("split_panel.splitter_grip");
        registerStyle(KrSplitPanel.class, splitPanelStyle);

        KrItemViewStyle listViewStyle = new KrItemViewStyle(widgetStyle);
        listViewStyle.gridColor = getColor(FOREGROUND);
        listViewStyle.gridVisible = false;
        registerStyle(KrListView.class, listViewStyle);

        KrItemViewStyle tableViewStyle = new KrItemViewStyle(widgetStyle);
        tableViewStyle.gridColor = getColor(BORDER);
        tableViewStyle.gridVisible = true;
        registerStyle(KrTableView.class, tableViewStyle);

        KrComboBoxStyle comboBoxStyle = new KrComboBoxStyle(widgetStyle);
        comboBoxStyle.background = getDrawable("combobox.background_normal");
        comboBoxStyle.pressedBackground = getDrawable("combobox.background_pressed");
        comboBoxStyle.padding = new KrPadding(4);
        registerStyle(KrComboBox.class, comboBoxStyle);

        KrWidgetStyle iconPanelStyle = new KrWidgetStyle(widgetStyle);
        iconPanelStyle.padding = new KrPadding(0, 3, 0, 3);
        iconPanelStyle.background = toolkit.getDrawable(KrColor.TRANSPARENT);
        registerStyle(KrIconPanel.class, iconPanelStyle);
    }

    public <S extends KrWidgetStyle> void registerStyle(Class<? extends KrWidget> widgetClass, S style) {
        styles.put(widgetClass, style);
    }

    public KrWidgetStyle getStyle(Class<? extends KrWidget> styleClass) {
        return styles.get(styleClass);
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
        SCROLLBAR_THUMB,
    }
}
