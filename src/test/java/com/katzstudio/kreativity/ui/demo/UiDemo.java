package com.katzstudio.kreativity.ui.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.KrColor;
import com.katzstudio.kreativity.ui.KrContext;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrCollapsiblePanel;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrScrollPanel;
import com.katzstudio.kreativity.ui.component.KrSpinner;
import com.katzstudio.kreativity.ui.component.KrTextField;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.layout.KrGridLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout.Constraint;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;

/**
 */
public class UiDemo extends Game {

    private static Drawable WIDGET_BACKGROUND;

    private static Drawable RED_DRAWABLE;

    private KrCanvas canvas;

    private SpriteBatch spriteBatch;

    public static void main(String[] args) {
        System.out.println("Running ui demo");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 500;
        config.height = 500;
        config.fullscreen = false;
        config.vSyncEnabled = true;
        config.title = "Kreativity UI Demo";
        new LwjglApplication(new UiDemo(), config);
    }

    @Override
    public void create() {
        gl.glEnable(GL_BLEND);
        gl.glDepthMask(true);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch = new SpriteBatch();

        WIDGET_BACKGROUND = KrToolkit.createColorDrawable(KrColor.rgb(0x252525));
        RED_DRAWABLE = KrToolkit.createColorDrawable(Color.RED);

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);

        KrSkin.instance().install();
        canvas = new KrCanvas();
        Gdx.input.setInputProcessor(canvas);

        // Labels
        KrLabel labelA = new KrLabel("Hello Word");
        labelA.setPadding(new KrPadding(5, 3));
        labelA.setPosition(10, 10);
        labelA.setSize(labelA.getPreferredSize());

        // Check Boxes
        KrCheckbox checkboxA = new KrCheckbox();
        checkboxA.setChecked(false);
        checkboxA.setPosition(40, 30);

        KrCheckbox checkboxB = new KrCheckbox();
        checkboxB.setChecked(true);
        checkboxB.setPosition(10, 30);

        // Buttons
        KrButton buttonA = new KrButton("Button A");
        buttonA.setSize(60, 20);
        buttonA.setPosition(100, 10);
        buttonA.addListener(() -> System.out.println("Button A clicked"));

        KrButton largeButton = new KrButton("Large Button");
        largeButton.setSize(100, 40);
        largeButton.setPosition(100, 40);
        largeButton.addListener(() -> System.out.println("Large Button clicked"));

        KrTextField textField = new KrTextField();
        textField.setSize(100, 21);
        textField.setPosition(210, 10);

        KrSpinner spinner = new KrSpinner();
        spinner.setSize(100, 21);
        spinner.setPosition(210, 40);


        // grid layout
        KrWidget form = new KrWidget();
        form.setLayout(new KrGridLayout(2, 3, 3));

        KrWidget usernameLabel = new KrLabel("Username");
        usernameLabel.setName("label.username");
        KrWidget usernameEdit = new KrTextField();
        usernameEdit.setName("textfield.username");
        KrWidget weight = new KrLabel("Weight");
        weight.setName("label.weight");
        KrWidget weightEdit = new KrSpinner();
        weightEdit.setName("spinner.weight");

        form.add(usernameLabel, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        form.add(usernameEdit);
        form.add(weight, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        form.add(weightEdit);
        form.setBounds(10, 100, 200, 60);
        form.setBounds(new Vector2(10, 100), form.getMinSize());

        canvas.getRootComponent().add(checkboxA);
        canvas.getRootComponent().add(checkboxB);
        canvas.getRootComponent().add(labelA);
        canvas.getRootComponent().add(buttonA);
        canvas.getRootComponent().add(largeButton);
        canvas.getRootComponent().add(textField);
        canvas.getRootComponent().add(spinner);
        canvas.getRootComponent().add(form);

        // Scroll Panel
//        KrScrollPanel scrollPanel = createKrScrollPanel(uiContext, 10, 690, 100, 100);

        // Collapsible Panel A
//        KrCollapsiblePanel collapsiblePanelA = createKrCollapsiblePanel(uiContext, 120, 690, 300, 100);
//        collapsiblePanelA.setName("Game Object");
//        collapsiblePanelA.setContent(createTable(uiContext, "First collapsible panel", 10));

        // Collapsible Panel B
//        KrCollapsiblePanel collapsiblePanelB = createKrCollapsiblePanel(uiContext, 120, 690, 300, 100);
//        collapsiblePanelB.setName("Sprite Graphics");
//        collapsiblePanelB.setContent(createTable(uiContext, "First collapsible panel", 5));

        // Collapsible Panel C
//        KrCollapsiblePanel collapsiblePanelC = createKrCollapsiblePanel(uiContext, 120, 690, 300, 100);
//        collapsiblePanelC.setName("Shape Collision");
//        collapsiblePanelC.setContent(createTable(uiContext, "First collapsible panel", 7));

        // Collapsible Panel Table
//        Table collapsiblePanelTable = new Table(uiContext.getSkin());
//        collapsiblePanelTable.add(collapsiblePanelA).fillX().top().padBottom(2).row();
//        collapsiblePanelTable.add(collapsiblePanelB).fillX().top().padBottom(2).row();
//        collapsiblePanelTable.add(collapsiblePanelC).fillX().top().row();
//        collapsiblePanelTable.add(new KrWidget(new Color(0, 0, 0, 0))).fill().expand().row();

//        KrScrollPanel collapsibleScrollPanel = new KrScrollPanel(uiContext, collapsiblePanelTable);
//        collapsibleScrollPanel.setBounds(120, 490, 200, 300);
//        collapsibleScrollPanel.setExpandX(true);
    }

    private KrScrollPanel createKrScrollPanel(KrContext uiContext, int x, int y, int w, int h) {
        Table table = createTable(uiContext, "text line ", 20);
        KrScrollPanel scrollPanel = new KrScrollPanel(uiContext, table);
        scrollPanel.setBounds(x, y, w, h);
        scrollPanel.setExpandX(true);
        scrollPanel.setBackground(WIDGET_BACKGROUND);
        return scrollPanel;
    }

    private KrCollapsiblePanel createKrCollapsiblePanel(KrContext uiContext, int x, int y, int w, int h) {
        Table table = createTable(uiContext, "Property ", 10);
        KrCollapsiblePanel collapsiblePanel = new KrCollapsiblePanel(uiContext, "Game Object");
        collapsiblePanel.setBounds(x, y, w, h);
        collapsiblePanel.setContent(table);
        collapsiblePanel.setBackground(WIDGET_BACKGROUND);
        return collapsiblePanel;
    }

    private Table createTable(KrContext uiContext, String prefix, int rows) {
        Table table = new Table(uiContext.getSkin());
        for (int i = 0; i < rows; ++i) {
            Label label = new Label(prefix + " " + i, uiContext.getSkin());
            KrToolkit.ensureUniqueStyle(label);
            table.add(label).fillX().expandX().padLeft(4).row();
        }
        return table;
    }

    @Override
    public void render() {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        canvas.update(Gdx.graphics.getDeltaTime());
        canvas.draw();
    }
}
