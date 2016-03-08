package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.google.common.collect.ImmutableMap;
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrComboBox;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrList;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrScrollBar;
import com.katzstudio.kreativity.ui.component.KrTextField;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_DARK;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_LIGHT;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.FOREGROUND;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SELECTION_BACKGROUND;

/**
 * Kreativity Skin
 */
public class KrSkin {

    private static final KrSkin KREATIVITY_SKIN = new KrSkin();

    private final static ImmutableMap<ColorKey, Color> colors = ImmutableMap.<ColorKey, Color>builder()
            .put(BACKGROUND_LIGHT, new Color(0x424242ff))
            .put(BACKGROUND_DARK, new Color(0x353535ff))
            .put(FOREGROUND, new Color(0xDDDDDDFF))
            .put(SELECTION_BACKGROUND, new Color(0x505050FF))
            .build();

    private final Map<String, Drawable> drawablePatches = new HashMap<>();

    @Getter private Skin skin;

    @Getter private BitmapFont fontAwesome;

    @Getter private BitmapFont defaultFont;

    @Getter private BitmapFont defaultFontBold;

    @Getter @Setter private KrPanel.Style panelStyle;

    @Getter @Setter private KrLabel.Style labelStyle;

    @Getter @Setter private KrCheckbox.Style checkboxStyle;

    @Getter @Setter private KrButton.Style buttonStyle;

    @Getter @Setter private KrTextField.Style textFieldStyle;

    @Getter @Setter private KrTextField.Style spinnerStyle;

    @Getter @Setter private KrScrollBar.Style verticalScrollBarStyle;

    @Getter @Setter private KrScrollBar.Style horizontalScrollBarStyle;

    @Getter private Texture skinTexture;

    private KrSkin() {
    }

    public static KrSkin instance() {
        return KREATIVITY_SKIN;
    }

    public void install() {

//        loadScene2DSkin(assetPath);

//        loadCursors();

        loadKreativitySkin();
    }

    private void loadKreativitySkin() {
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonSkin = jsonReader.parse(Gdx.files.internal("ui/skin.json"));

        String textureName = jsonSkin.get("texture").asString();
        skinTexture = new Texture(Gdx.files.internal("ui/" + textureName));

        String defaultFontPath = jsonSkin.get("fonts").get("default").asString();
        defaultFont = new BitmapFont(Gdx.files.internal("ui/" + defaultFontPath));

        String boldFontPath = jsonSkin.get("fonts").get("bold").asString();
        defaultFontBold = new BitmapFont(Gdx.files.internal("ui/" + boldFontPath));

        String fontAwesomePath = jsonSkin.get("fonts").get("fontawesome").asString();
        fontAwesome = new BitmapFont(Gdx.files.internal("ui/" + fontAwesomePath));

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

        panelStyle = new KrPanel.Style(KrToolkit.createColorDrawable(KrColor.TRANSPARENT));

        labelStyle = new KrLabel.Style(
                KrToolkit.createColorDrawable(KrColor.TRANSPARENT),
                defaultFont,
                KrColor.rgb(0xffffff));

        buttonStyle = new KrButton.Style(
                drawablePatches.get("button.background_normal"),
                drawablePatches.get("button.background_hovered"),
                drawablePatches.get("button.background_armed"),
                defaultFont,
                getColor(FOREGROUND),
                new Vector2(0, 1),
                new Color(0x00000060));

        checkboxStyle = new KrCheckbox.Style(
                drawablePatches.get("checkbox.background"),
                drawablePatches.get("checkbox.mark"));

        textFieldStyle = new KrTextField.Style(
                drawablePatches.get("textfield.background_normal"),
                drawablePatches.get("textfield.background_hovered"),
                drawablePatches.get("textfield.background_focused"),
                defaultFont,
                getColor(FOREGROUND),
                getColor(FOREGROUND),
                KrColor.rgb(0x38466b));

        textFieldStyle = new KrTextField.Style(
                drawablePatches.get("textfield.background_normal"),
                drawablePatches.get("textfield.background_hovered"),
                drawablePatches.get("textfield.background_focused"),
                defaultFont,
                getColor(FOREGROUND),
                getColor(FOREGROUND),
                KrColor.rgb(0x38466b));

        spinnerStyle = new KrTextField.Style(
                drawablePatches.get("spinner.background_normal"),
                drawablePatches.get("spinner.background_hovered"),
                drawablePatches.get("spinner.background_focused"),
                defaultFont,
                getColor(FOREGROUND),
                getColor(FOREGROUND),
                KrColor.rgb(0x38466b));

        verticalScrollBarStyle = new KrScrollBar.Style(
                drawablePatches.get("scrollbar.vertical.track"),
                drawablePatches.get("scrollbar.vertical.thumb"),
                4,
                new KrPadding(0));

        horizontalScrollBarStyle = new KrScrollBar.Style(
                drawablePatches.get("scrollbar.horizontal.track"),
                drawablePatches.get("scrollbar.horizontal.thumb"),
                4,
                new KrPadding(0));
    }

    private void loadCursors() {
        for (Cursor cursor : Cursor.values()) {
            cursor.setPixmap(new Pixmap(Gdx.files.internal(cursor.filePath)));
        }

        KrToolkit.setCursor(Cursor.POINTER);
    }

    private void loadScene2DSkin(String assetPath) {
        skin = new Skin(Gdx.files.internal(assetPath + "ui/skin.json"));

        // Scroll Pane Style
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(
                new NinePatchDrawable(skin.getPatch("scrollpanel-background")),
                new NinePatchDrawable(skin.getPatch("scrollpanel.scroll.track")),
                new NinePatchDrawable(skin.getPatch("scrollpanel.scroll.thumb")),
                new NinePatchDrawable(skin.getPatch("scrollpanel.scroll.track")),
                new NinePatchDrawable(skin.getPatch("scrollpanel.scroll.thumb")));
        scrollPaneStyle.vScrollKnob.setMinWidth(9);
        scrollPaneStyle.vScroll.setMinWidth(9);

        // Label Style
        Label.LabelStyle oldLabelStyle = new Label.LabelStyle(defaultFont, colors.get(ColorKey.FOREGROUND));

        // Item List Style
        KrList.Style itemListStyle = KrList.Style.builder()
                .background(new NinePatchDrawable(skin.getPatch("list-background")))
                .selection(new NinePatchDrawable(skin.getPatch("selection")))
                .paddingLeft(4)
                .font(defaultFont)
                .build();

        // ComboBox Style
        ScrollPane.ScrollPaneStyle comboBoxScrollStyle = new ScrollPane.ScrollPaneStyle(scrollPaneStyle);
        comboBoxScrollStyle.background = new NinePatchDrawable(new NinePatch(skin.getPatch("combobox-list")));

        KrComboBox.Style comboBoxStyle = KrComboBox.Style.builder()
                .background(new NinePatchDrawable(skin.getPatch("default-select")))
                .itemListStyle(itemListStyle)
                .scrollStyle(comboBoxScrollStyle)
                .font(defaultFont)
                .build();

        TextField.TextFieldStyle textFieldStyle = skin.get("default", TextField.TextFieldStyle.class);
        textFieldStyle.font = defaultFont;

        skin.add("default", oldLabelStyle);
        skin.add("default", itemListStyle);
        skin.add("default", comboBoxStyle);
        skin.add("default", scrollPaneStyle);
    }

    private Rectangle jsonArrayToRectangle(JsonValue jsonValue) {
        return new Rectangle(jsonValue.get(0).asInt(), jsonValue.get(1).asInt(), jsonValue.get(2).asInt(), jsonValue.get(3).asInt());
    }

    public static Color getColor(ColorKey key) {
        return colors.get(key);
    }

    public enum ColorKey {
        BACKGROUND_LIGHT,
        BACKGROUND_DARK,
        FOREGROUND,
        SELECTION_BACKGROUND,
    }

    public enum Cursor {
        POINTER("ui/cursors/arrow.png", 0, 0),
        RESIZE_V("ui/cursors/resize_v.png", 3, 4);

        private final String filePath;

        @Getter
        private final int hotSpotX;

        @Getter
        private final int hotSpotY;

        @Getter
        private Pixmap cursorPixmap;

        Cursor(String filePath, int hotSpotX, int hotSpotY) {
            this.filePath = filePath;
            this.hotSpotX = hotSpotX;
            this.hotSpotY = hotSpotY;
        }

        private void setPixmap(Pixmap pixmap) {
            cursorPixmap = pixmap;
        }
    }
}
