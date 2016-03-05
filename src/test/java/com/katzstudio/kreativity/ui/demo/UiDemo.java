package com.katzstudio.kreativity.ui.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.KrColor;
import com.katzstudio.kreativity.ui.KrContext;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.KrUnifiedSize;
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrCollapsiblePanel;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrScrollPanel;
import com.katzstudio.kreativity.ui.component.KrSpinner;
import com.katzstudio.kreativity.ui.component.KrTextField;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout.Constraint;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;
import static com.katzstudio.kreativity.ui.layout.KrFlowLayout.Direction.HORIZONTAL;
import static com.katzstudio.kreativity.ui.layout.KrFlowLayout.Direction.VERTICAL;

/**
 * Demo / testing application.
 */
public class UiDemo extends Game {

    private static Drawable WIDGET_BACKGROUND;

    private KrCanvas canvas;

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
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

        WIDGET_BACKGROUND = KrToolkit.createColorDrawable(KrColor.rgb(0x252525));

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
        KrButton buttonA = new KrButton("Triangulate");
        buttonA.setSize(100, 24);
        buttonA.setPosition(100, 10);
        buttonA.addListener(() -> System.out.println("Button A clicked"));

        KrButton largeButton = new KrButton("Large Button");
        largeButton.setSize(100, 40);
        largeButton.setPosition(100, 40);
        largeButton.addListener(() -> System.out.println("Large Button clicked"));

        KrTextField textField = new KrTextField();
        textField.setName("textfield.test");
        textField.setSize(100, 21);
        textField.setPosition(210, 10);

        KrSpinner spinner = new KrSpinner();
        spinner.setName("spinner.test");
        spinner.setSize(100, 21);
        spinner.setPosition(210, 40);

        // grid layout
        KrWidget gridLayoutWidget = createGridLayout();

        KrWidget horizontalFlowLayout = createHorizontalFlowLayout();

        KrWidget verticalFlowLayout = createVerticalFlowLayout();

        canvas.getRootComponent().add(checkboxA);
        canvas.getRootComponent().add(checkboxB);
        canvas.getRootComponent().add(labelA);
        canvas.getRootComponent().add(buttonA);
        canvas.getRootComponent().add(largeButton);
        canvas.getRootComponent().add(textField);
        canvas.getRootComponent().add(spinner);
        canvas.getRootComponent().add(gridLayoutWidget);
        canvas.getRootComponent().add(horizontalFlowLayout);
        canvas.getRootComponent().add(verticalFlowLayout);

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

    private KrWidget createVerticalFlowLayout() {
        KrWidget panel = new KrPanel();
        KrPanel.Style style = (KrPanel.Style) panel.getStyle();

        panel.setLayout(new KrFlowLayout(VERTICAL, 5, 2));

        KrLabel labelA = new KrLabel("V Flow Label A");
        labelA.setName("flowlayoutV.labelA");

        KrLabel labelB = new KrLabel("V Flow Label B");
        labelB.setName("flowlayoutV.labelB");

        KrLabel labelC = new KrLabel("V Flow Label C");
        labelC.setName("flowlayoutV.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);
        panel.setBounds(new Vector2(10, 150), panel.getPreferredSize());
        return panel;
    }

    private KrWidget createHorizontalFlowLayout() {
        KrWidget panel = new KrPanel();
        panel.setLayout(new KrFlowLayout(HORIZONTAL, 5, 2));

        KrLabel labelA = new KrLabel("Label A");
        labelA.setName("flowlayoutH.labelA");

        KrLabel labelB = new KrLabel("Some very long label");
        labelB.setName("flowlayoutH.labelB");

        KrLabel labelC = new KrLabel("X");
        labelC.setName("flowlayoutH.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);
        panel.setBounds(190, 100, 180, 20);
        return panel;
    }

    private KrWidget createGridLayout() {
        KrWidget form = new KrWidget();
        KrGridLayout formLayout = new KrGridLayout(2, 5, 3);
        formLayout.setColumnSizePolicy(new KrSizePolicyModel(new KrUnifiedSize(55, 0), new KrUnifiedSize(80, 1)));
        form.setLayout(formLayout);

        KrWidget usernameLabel = new KrLabel("Username");
        usernameLabel.setName("label.username");
        KrWidget usernameEdit = new KrTextField();
        usernameEdit.setName("textfield.username");
        KrWidget weight = new KrLabel("Weight");
        weight.setName("label.weight");
        KrWidget weightEdit = new KrSpinner();
        weightEdit.setName("spinner.weight");

        form.add(usernameLabel, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        form.add(usernameEdit, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        form.add(weight, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        form.add(weightEdit, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        form.setBounds(10, 100, 200, 60);
        form.setBounds(new Vector2(10, 100), form.getMinSize());
        return form;
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

    @Override
    public void resize(int width, int height) {
        canvas.setSize(width, height);
    }
}
