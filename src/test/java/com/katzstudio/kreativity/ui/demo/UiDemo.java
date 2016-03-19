package com.katzstudio.kreativity.ui.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrUnifiedSize;
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrButtonGroup;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrListView;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrScrollBar;
import com.katzstudio.kreativity.ui.component.KrScrollPanel;
import com.katzstudio.kreativity.ui.component.KrSpinner;
import com.katzstudio.kreativity.ui.component.KrSplitPanel;
import com.katzstudio.kreativity.ui.component.KrTextField;
import com.katzstudio.kreativity.ui.component.KrToggleButton;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.listener.KrMouseListener;
import com.katzstudio.kreativity.ui.layout.KrAbsoluteLayout;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout.Constraint;
import com.katzstudio.kreativity.ui.model.KrListItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;
import static com.katzstudio.kreativity.ui.KrColor.rgb;
import static com.katzstudio.kreativity.ui.KrOrientation.HORIZONTAL;
import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;
import static com.katzstudio.kreativity.ui.KrToolkit.getDrawable;

/**
 * Demo / testing application.
 */
public class UiDemo extends Game {

    private Drawable DARK_GRAY;

    private Drawable DARKER_GRAY;

    private Color lightGray;

    private KrCanvas canvas;

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 840;
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

        DARK_GRAY = getDrawable(rgb(0x434343));

        DARKER_GRAY = getDrawable(rgb(0x393939));

        lightGray = rgb(0xaaaaaa);

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);

        KrSkin.instance().install();
        canvas = new KrCanvas();
        Gdx.input.setInputProcessor(canvas);

        canvas.getRootComponent().add(createButtons());
        canvas.getRootComponent().add(createGridLayout());
        canvas.getRootComponent().add(createCheckboxes());
        canvas.getRootComponent().add(createHorizontalFlowLayoutPanel());
        canvas.getRootComponent().add(createVerticalFlowLayoutPanel());
        canvas.getRootComponent().add(createBorderLayoutPanel());
        canvas.getRootComponent().add(createScrollBarsPanel());
        canvas.getRootComponent().add(createScrollPanel());
        canvas.getRootComponent().add(createSplitPanel());
        canvas.getRootComponent().add(createListView());
    }

    private KrPanel createListView() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("List View");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setName("list_view.label");
        label.setGeometry(0, 0, 60, 20);

        List<String> itemValues = new ArrayList<>(15);
        for (int i = 0; i < 15; ++i) {
            itemValues.add("Item " + i);
        }
        KrListItemModel<String> model = new KrListItemModel<>(itemValues);
        KrListView listView = new KrListView(model);
        listView.setGeometry(0, 20, 160, 100);

        wrapper.add(label);
        wrapper.add(listView);
        wrapper.setGeometry(385, 260, 160, 120);
        return wrapper;
    }


    private KrPanel createCheckboxes() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Checkboxes");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setName("buttons.label");
        label.setGeometry(0, 0, 60, 20);

        KrCheckbox checkboxA = new KrCheckbox();
        checkboxA.setPosition(10, 20);
        checkboxA.setChecked(true);

        KrCheckbox checkboxB = new KrCheckbox();
        checkboxB.setPosition(40, 20);

        wrapper.add(label);
        wrapper.add(checkboxA);
        wrapper.add(checkboxB);

        wrapper.setGeometry(10, 250, 60, 50);
        return wrapper;
    }

    private KrWidget createButtons() {
        KrLabel label = new KrLabel("Buttons");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setName("buttons.label");
        label.setGeometry(0, 0, 100, 20);

        KrButton button = new KrButton("Push Button");
        button.setName("buttons.button");
        button.setGeometry(0, 20, 100, 26);
        button.addMouseListener(new KrMouseListener.KrMouseAdapter() {
            @Override
            public void mouseDoubleClicked(KrMouseEvent event) {
                System.out.println("event = " + event);
            }
        });

        KrToggleButton toggleButton = new KrToggleButton("Toggle Button");
        toggleButton.setName("buttons.toggleButton");
        toggleButton.setGeometry(0, 50, 100, 26);

        KrLabel groupLabel = new KrLabel("Toggle Button Group");
        groupLabel.ensureUniqueStyle();
        ((KrLabel.Style) groupLabel.getStyle()).foregroundColor = lightGray;
        groupLabel.setName("buttons.groupLabel");
        groupLabel.setGeometry(0, 85, 160, 20);

        KrToggleButton first = new KrToggleButton("RGB");
        first.setName("toggle_group.first");
        KrToggleButton second = new KrToggleButton("HSV");
        second.setName("toggle_group.second");
        KrToggleButton third = new KrToggleButton("CMYK");
        third.setName("toggle_group.third");

        KrButtonGroup buttonGroup = new KrButtonGroup(first, second, third);
        buttonGroup.setGeometry(0, 105, 160, 26);
        buttonGroup.setAllowUncheck(false);

        KrPanel panel = new KrPanel();
        panel.setName("buttons.container");
        panel.setLayout(new KrAbsoluteLayout());
        panel.add(label);
        panel.add(button);
        panel.add(toggleButton);
        panel.add(groupLabel);
        panel.add(buttonGroup);
        panel.setGeometry(10, 10, 160, 140);

        return panel;
    }

    private KrWidget createSplitPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Split Panel");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setGeometry(0, 0, 150, 20);

        KrSplitPanel splitPanel = new KrSplitPanel();
        splitPanel.add(createDummyContent(), new KrUnifiedSize(60, 1));
        splitPanel.add(createDummyContent(), new KrUnifiedSize(60, 1));
        splitPanel.add(createDummyContent(), new KrUnifiedSize(60, 1));

        splitPanel.setGeometry(0, 20, 150, 300);

        wrapper.add(label);
        wrapper.add(splitPanel);
        wrapper.setGeometry(560, 10, 150, 320);

        return wrapper;
    }

    private KrWidget createScrollPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Scroll Panel");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setGeometry(0, 0, 150, 20);

        KrWidget panel = createDummyContent();
        panel.setPreferredSize(new Vector2(120, 200));
        KrScrollPanel scrollPanel = new KrScrollPanel(panel);
        scrollPanel.setGeometry(0, 20, 160, 90);

        wrapper.add(label);
        wrapper.add(scrollPanel);
        wrapper.setGeometry(385, 140, 160, 120);

        return wrapper;
    }

    private KrPanel createScrollBarsPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Scroll Bars");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setGeometry(0, 0, 150, 20);

        KrPanel verticalScrollBarPanel = createVerticalScrollBarPanel();
        KrPanel horizontalScrollBarPanel = createHorizontalScrollBarPanel();

        wrapper.add(label);
        wrapper.add(verticalScrollBarPanel);
        wrapper.add(horizontalScrollBarPanel);

        wrapper.setGeometry(385, 10, 160, 120);

        return wrapper;
    }

    private KrPanel createVerticalScrollBarPanel() {
        KrScrollBar scrollBar = new KrScrollBar(VERTICAL);
        scrollBar.setPreferredSize(new Vector2(6, 100));
        scrollBar.setName("scrollbar.vertical");

        KrLabel label = new KrLabel(String.valueOf((int) scrollBar.getCurrentValue()));
        label.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        label.setName("scrollbar.vertical.value_label");

        scrollBar.addScrollListener(value -> label.setText(String.valueOf((int) value)));

        KrPanel result = new KrPanel();
        result.setLayout(new KrBorderLayout());
        result.add(scrollBar, KrBorderLayout.Constraint.CENTER);
        result.add(label, KrBorderLayout.Constraint.SOUTH);
        result.setGeometry(0, 20, 30, 100);

        return result;
    }

    private KrPanel createHorizontalScrollBarPanel() {
        KrScrollBar scrollBar = new KrScrollBar(HORIZONTAL);
        scrollBar.setPreferredSize(new Vector2(100, 6));
        scrollBar.setName("scrollbar.horizontal");

        KrLabel label = new KrLabel(String.valueOf((int) scrollBar.getCurrentValue()));
        label.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        label.setPreferredSize(new Vector2(30, 20));
        label.setName("scrollbar.horizontal.value_label");

        scrollBar.addScrollListener(value -> label.setText(String.valueOf((int) value)));

        KrPanel result = new KrPanel();
        result.setLayout(new KrBorderLayout());
        result.add(scrollBar, KrBorderLayout.Constraint.CENTER);
        result.add(label, KrBorderLayout.Constraint.EAST);
        result.setGeometry(50, 20, 100, 30);

        return result;
    }

    private KrWidget createHorizontalFlowLayoutPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Horizontal Flow Layout");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setGeometry(0, 0, 180, 20);

        KrPanel panel = new KrPanel();
        panel.ensureUniqueStyle();
        ((KrPanel.Style) panel.getStyle()).background = DARK_GRAY;

        panel.setLayout(new KrFlowLayout(HORIZONTAL, 5, 5));

        KrLabel labelA = new KrLabel("Label A");
        labelA.ensureUniqueStyle();
        ((KrLabel.Style) labelA.getStyle()).background = DARKER_GRAY;
        labelA.setName("flowlayoutH.labelA");

        KrLabel labelB = new KrLabel("Some very long label");
        labelB.ensureUniqueStyle();
        ((KrLabel.Style) labelB.getStyle()).background = DARKER_GRAY;
        labelB.setName("flowlayoutH.labelB");

        KrLabel labelC = new KrLabel("X");
        labelC.ensureUniqueStyle();
        ((KrLabel.Style) labelC.getStyle()).background = DARKER_GRAY;
        labelC.setName("flowlayoutH.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);
        panel.setGeometry(0, 20, 180, 40);

        wrapper.add(label);
        wrapper.add(panel);
        wrapper.setGeometry(190, 10, 180, 60);
        return wrapper;
    }

    private KrWidget createVerticalFlowLayoutPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Vertical Flow Layout");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setGeometry(0, 0, 180, 20);

        KrWidget panel = new KrPanel();
        panel.ensureUniqueStyle();
        ((KrPanel.Style) panel.getStyle()).background = DARK_GRAY;

        panel.setLayout(new KrFlowLayout(VERTICAL, 5, 5));

        KrLabel labelA = new KrLabel("First Row");
        labelA.ensureUniqueStyle();
        ((KrLabel.Style) labelA.getStyle()).background = DARKER_GRAY;
        labelA.setName("flowlayoutV.labelA");

        KrLabel labelB = new KrLabel("Second Row");
        labelB.ensureUniqueStyle();
        ((KrLabel.Style) labelB.getStyle()).background = DARKER_GRAY;
        labelB.setName("flowlayoutV.labelB");

        KrLabel labelC = new KrLabel("Third Row");
        labelC.ensureUniqueStyle();
        ((KrLabel.Style) labelC.getStyle()).background = DARKER_GRAY;
        labelC.setName("flowlayoutV.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);
        panel.setGeometry(0, 20, 180, 120);

        wrapper.add(label);
        wrapper.add(panel);
        wrapper.setGeometry(190, 80, 180, 140);

        return wrapper;
    }

    private KrWidget createBorderLayoutPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Border Layout");
        label.ensureUniqueStyle();
        ((KrLabel.Style) label.getStyle()).foregroundColor = lightGray;
        label.setGeometry(0, 0, 180, 20);

        KrWidget panel = new KrPanel();
        panel.ensureUniqueStyle();
        ((KrPanel.Style) panel.getStyle()).background = DARK_GRAY;
        panel.setLayout(new KrBorderLayout(4, 4));

        KrLabel topWidget = new KrLabel("Top");
        topWidget.setName("borderlayout.top");
        topWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        topWidget.ensureUniqueStyle();
        ((KrLabel.Style) topWidget.getStyle()).background = DARKER_GRAY;
        topWidget.setPreferredSize(new Vector2(100, 40));

        KrLabel bottomWidget = new KrLabel("Bottom");
        bottomWidget.setName("borderlayout.bottom");
        bottomWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        bottomWidget.ensureUniqueStyle();
        ((KrLabel.Style) bottomWidget.getStyle()).background = DARKER_GRAY;
        bottomWidget.setPreferredSize(new Vector2(100, 20));

        KrLabel leftWidget = new KrLabel("W");
        leftWidget.setName("borderlayout.left");
        leftWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        leftWidget.ensureUniqueStyle();
        ((KrLabel.Style) leftWidget.getStyle()).background = DARKER_GRAY;
        leftWidget.setPreferredSize(new Vector2(30, 20));

        KrLabel rightWidget = new KrLabel("E");
        rightWidget.setName("borderlayout.right");
        rightWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        rightWidget.ensureUniqueStyle();
        ((KrLabel.Style) rightWidget.getStyle()).background = DARKER_GRAY;
        rightWidget.setPreferredSize(new Vector2(20, 20));

        KrLabel centerWidget = new KrLabel("CENTER");
        centerWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        centerWidget.setName("borderlayout.center");
        centerWidget.ensureUniqueStyle();
        ((KrLabel.Style) centerWidget.getStyle()).background = DARKER_GRAY;
        centerWidget.setPreferredSize(new Vector2(100, 20));

        panel.add(topWidget, KrBorderLayout.Constraint.NORTH);
        panel.add(bottomWidget, KrBorderLayout.Constraint.SOUTH);
        panel.add(leftWidget, KrBorderLayout.Constraint.WEST);
        panel.add(rightWidget, KrBorderLayout.Constraint.EAST);
        panel.add(centerWidget, KrBorderLayout.Constraint.CENTER);

        panel.setGeometry(0, 20, 180, 120);

        wrapper.add(label);
        wrapper.add(panel);
        wrapper.setGeometry(190, 230, 180, 140);

        return wrapper;
    }

    private KrWidget createGridLayout() {
        KrPanel form = new KrPanel();

        KrLabel formLabel = new KrLabel("Grid Layout / Text Field / Spinner");
        formLabel.ensureUniqueStyle();
        ((KrLabel.Style) formLabel.getStyle()).foregroundColor = lightGray;
        formLabel.setName("grid_layout.form_label");
        formLabel.setPosition(0, 0);

        KrPanel fields = new KrPanel();
        KrGridLayout formLayout = new KrGridLayout(2, 5, 3);
        formLayout.setColumnSizePolicy(new KrSizePolicyModel(new KrUnifiedSize(55, 0), new KrUnifiedSize(80, 1)));
        fields.setLayout(formLayout);

        KrWidget usernameLabel = new KrLabel("Username");
        usernameLabel.setName("grid_layout.label.username");
        KrWidget usernameEdit = new KrTextField();
        usernameEdit.setName("grid_layout.textfield.username");
        KrWidget weight = new KrLabel("Weight");
        weight.setName("grid_layout.label.weight");
        KrSpinner weightEdit = new KrSpinner();
        weightEdit.setName("grid_layout.spinner.weight");
        KrWidget weightClone = new KrLabel("Weight 2");
        weightClone.setName("grid_layout.label.weight_clone");
        KrSpinner weightEditClone = new KrSpinner();
        weightEditClone.setName("grid_layout.spinner.weight_clone");
        weightEditClone.setSpinnerModel(weightEdit.getSpinnerModel());

        fields.add(usernameLabel, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        fields.add(usernameEdit, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        fields.add(weight, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        fields.add(weightEdit, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        fields.add(weightClone, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        fields.add(weightEditClone, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        fields.setGeometry(new Vector2(0, 20), fields.getMinSize());

        form.add(formLabel);
        form.add(fields);

        form.setGeometry(10, 150, 1200, 110);

        return form;
    }

    public KrPanel createDummyContent() {
        KrPanel panel = new KrPanel();
        panel.ensureUniqueStyle();
        ((KrPanel.Style) panel.getStyle()).background = DARK_GRAY;

        panel.setLayout(new KrFlowLayout(VERTICAL, 5, 5));

        KrLabel labelA = new KrLabel("First Row");
        labelA.ensureUniqueStyle();
        ((KrLabel.Style) labelA.getStyle()).background = DARKER_GRAY;
        labelA.setName("flowlayoutV.labelA");

        KrLabel labelB = new KrLabel("Second Row");
        labelB.ensureUniqueStyle();
        ((KrLabel.Style) labelB.getStyle()).background = DARKER_GRAY;
        labelB.setName("flowlayoutV.labelB");

        KrLabel labelC = new KrLabel("Third Row");
        labelC.ensureUniqueStyle();
        ((KrLabel.Style) labelC.getStyle()).background = DARKER_GRAY;
        labelC.setName("flowlayoutV.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);

        return panel;
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
