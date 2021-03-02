package com.katzstudio.kreativity.ui.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.katzstudio.kreativity.ui.*;
import com.katzstudio.kreativity.ui.backend.gdx.KrGdxBackend;
import com.katzstudio.kreativity.ui.component.*;
import com.katzstudio.kreativity.ui.component.KrMenu.KrMenuItem;
import com.katzstudio.kreativity.ui.component.KrMenu.KrMenuItemSeparator;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.listener.KrMouseListener;
import com.katzstudio.kreativity.ui.icon.KrFontAwesomeIcon;
import com.katzstudio.kreativity.ui.icon.KrIcon;
import com.katzstudio.kreativity.ui.layout.KrAbsoluteLayout;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;
import com.katzstudio.kreativity.ui.layout.KrCardLayout;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout;
import com.katzstudio.kreativity.ui.layout.KrGridLayout.Constraint;
import com.katzstudio.kreativity.ui.model.KrItemModel;
import com.katzstudio.kreativity.ui.model.KrListItemModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import java.util.ArrayList;
import java.util.Arrays;
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
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;
import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.CENTER;
import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.NORTH;

/**
 * Demo / testing application.
 */
@SuppressWarnings("unused")
public class UiDemo extends Game {

    @SuppressWarnings("unused")
    private final FPSLogger fpsLogger = new FPSLogger();

    private Drawable darkGray;

    private Drawable darkerGray;

    private Color lightGray;

    private KrCanvas canvas;

    private KrPopup testPopup;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(940, 600);
        config.setTitle("Kreativity UI Demo");
        config.useVsync(false);
        new Lwjgl3Application(new UiDemo(), config);
    }

    @Override
    public void create() {
        gl.glEnable(GL_BLEND);
        gl.glDepthMask(true);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);

        KrToolkit.initialize(new KrGdxBackend());
        Gdx.input.setInputProcessor((InputAdapter) getDefaultToolkit().getInputSource());
        canvas = KrToolkit.getDefaultToolkit().getCanvas();

        darkGray = getDefaultToolkit().getDrawable(rgb(0x434343));
        darkerGray = getDefaultToolkit().getDrawable(rgb(0x393939));
        lightGray = rgb(0xaaaaaa);

        testPopup = createPopup();

        canvas.addInputListener((widget, event) -> {
            if (event instanceof KrMouseEvent) {
                KrMouseEvent mouseEvent = (KrMouseEvent) event;
                if (mouseEvent.getType() == KrMouseEvent.Type.PRESSED && mouseEvent.getButton() == KrMouseEvent.Button.RIGHT) {
                    testPopup.show(mouseEvent.getScreenPosition());
                }
                if (widget == canvas.getRootPanel() && mouseEvent.getType() == KrMouseEvent.Type.PRESSED && mouseEvent.getButton() == KrMouseEvent.Button.LEFT) {
                    testPopup.hide();
                }
            }
        });

        KrDemoPanel demoPanel = new KrDemoPanel();

        canvas.getRootPanel().setLayout(new KrBorderLayout());
        canvas.getRootPanel().add(demoPanel, KrBorderLayout.Constraint.CENTER);

        demoPanel.addChild(createButtons(), 0);
        demoPanel.addChild(createGridLayout(), 0);
        demoPanel.addChild(createCheckbox(), 0);
        demoPanel.addChild(createSlider(), 0);

        demoPanel.addChild(createHorizontalFlowLayoutPanel(), 1);
        demoPanel.addChild(createVerticalFlowLayoutPanel(), 1);
        demoPanel.addChild(createBorderLayoutPanel(), 1);
        demoPanel.addChild(createCardLayout(), 1);

        demoPanel.addChild(createScrollBarsPanel(), 2);
        demoPanel.addChild(createScrollPanel(), 2);
        demoPanel.addChild(createListView(), 2);
        demoPanel.addChild(createTableView(), 2);

        demoPanel.addChild(createSplitPanel(), 3);
        demoPanel.addChild(createCollapsiblePanel(), 3);
        demoPanel.addChild(createImagePanel(), 3);

//        canvas.getRootPanel().add(createSandbox());
    }

    private KrWidget createImagePanel() {
        KrCollapsiblePanel collapsiblePanel = new KrCollapsiblePanel("Collapsible Image Panel");

        KrPanel bodyPanel = collapsiblePanel.getBodyPanel();
        bodyPanel.setLayout(new KrBorderLayout());

        Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        KrDrawableView imagePanel = new KrDrawableView(drawable);
        imagePanel.setKeepAspect(false);
        // Force image size to 256 x 256
        imagePanel.setGeometry(0,0, texture.getWidth(), texture.getHeight());

        bodyPanel.add(imagePanel, KrBorderLayout.Constraint.CENTER);

        return collapsiblePanel;
    }

    private KrWidget createSlider() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Sliders");
        label.setForeground(lightGray);
        label.setName("sliders.label");
        label.setGeometry(0, 0, 60, 20);

        KrSlider slider = new KrSlider();
        slider.setPosition(10, 20);
        slider.setSize(slider.getPreferredSize());

        wrapper.add(label);
        wrapper.add(slider);

        wrapper.setPreferredHeight(50);
        return wrapper;
    }

    private KrCollapsiblePanel createCollapsiblePanel() {
        KrCollapsiblePanel collapsiblePanel = new KrCollapsiblePanel("Collapsible Panel");

        KrPanel bodyPanel = collapsiblePanel.getBodyPanel();
        bodyPanel.setLayout(new KrBorderLayout());
        KrWidget contentPanel = createBorderLayoutPanel();
        contentPanel.setBackground(getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND_LIGHT));
        bodyPanel.add(contentPanel, KrBorderLayout.Constraint.CENTER);

        return collapsiblePanel;
    }

    private KrWidget createSandbox() {
        return new KrWidget() {
            KrIcon icon = new KrFontAwesomeIcon(KrFontAwesomeGlyph.CHEVRON_CIRCLE_LEFT);

            {
                setGeometry(0, 500, 14, 14);
            }

            @Override
            protected void drawSelf(KrRenderer renderer) {
                renderer.setBrush(new Color(0x242424ff));
                renderer.fillRect(0, 0, getWidth(), getHeight());
                icon.draw(renderer, 0, 0);
            }
        };
    }

    private KrMenu createMenu() {
        KrMenu menu = new KrMenu();

        KrMenuItem menuItem1 = new KrMenuItem("Add", null);
        KrMenuItem menuItem2 = new KrMenuItem("Remove", null);
        KrMenuItem menuItem3 = new KrMenuItem("Explain", null);

        menu.addMenuItem(menuItem1);
        menu.addMenuItem(menuItem2);
        menu.addMenuItem(menuItem3);
        menu.addMenuItem(new KrMenuItemSeparator());
        menu.addMenuItem(new KrMenuItem("Save"));
        menu.addMenuItem(new KrMenuItem("Save All"));
        menu.addMenuItem(new KrMenuItem("Export"));

        menu.setPreferredSize(new Vector2(100, menu.getPreferredHeight()));

        return menu;
    }

    private KrPopup createPopup() {
        KrToolkit toolkit = getDefaultToolkit();

        KrPopup popup = new KrPopup();
        KrWidget stackLayout = createCardLayout();
        stackLayout.ensureUniqueStyle();
        stackLayout.getStyle().background = toolkit.getDrawable(toolkit.getSkin().getColor(KrSkin.ColorKey.BACKGROUND_DARK));
        stackLayout.setPreferredSize(new Vector2(180, 100));
        stackLayout.setPadding(new KrPadding(4, 4, 4, 4));
        popup.setContentWidget(stackLayout);
        return popup;
    }

    private KrWidget createCardLayout() {
        KrPanel wrapper = new KrPanel();

        KrLabel titleLabel = new KrLabel("Card Layout");
        titleLabel.setForeground(lightGray);
        titleLabel.setName("list_view.titleLabel");
        titleLabel.setGeometry(0, 0, 180, 20);

        KrCardLayout cardLayout = new KrCardLayout();
        KrPanel layoutContainer = new KrPanel(cardLayout);
        layoutContainer.add(createDummyLabel("Card A"), 0);
        layoutContainer.add(createDummyLabel("Card B"), 1);
        layoutContainer.add(createDummyLabel("Card C"), 2);
        layoutContainer.setGeometry(0, 42, 180, 58);

        KrToggleButton cardASelector = new KrToggleButton("Card A");
        cardASelector.addToggleListener(isChecked -> {
            if (isChecked) {
                cardLayout.setCard(0);
            }
        });
        cardASelector.setTooltipText("Select first card");

        KrToggleButton cardBSelector = new KrToggleButton("Card B");
        cardBSelector.addToggleListener(isChecked -> {
            if (isChecked) {
                cardLayout.setCard(1);
            }
        });
        cardBSelector.setTooltipText("Select second card");

        KrToggleButton cardCSelector = new KrToggleButton("Card C");
        cardCSelector.addToggleListener(isChecked -> {
            if (isChecked) {
                cardLayout.setCard(2);
            }
        });
        cardCSelector.setTooltipWidget(createTooltipWidget());

        KrButtonGroup cardSelector = new KrButtonGroup(cardASelector, cardBSelector, cardCSelector);
        cardSelector.setAllowUnCheck(false);
        cardSelector.setGeometry(0, 20, 180, 20);

        wrapper.add(titleLabel);
        wrapper.add(cardSelector);
        wrapper.add(layoutContainer);

        wrapper.setPreferredHeight(100);

        return wrapper;
    }

    private KrWidget createTooltipWidget() {
        KrPanel tooltipWidget = new KrPanel();

        tooltipWidget.setLayout(new KrFlowLayout(VERTICAL));

        tooltipWidget.add(new KrLabel("First row"));
        tooltipWidget.add(new KrLabel("Second row"));
        tooltipWidget.add(new KrLabel("the end"));

        return tooltipWidget;
    }

    private KrLabel createDummyLabel(String text) {
        KrLabel label = new KrLabel(text);
        label.setBackground(darkGray);
        label.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        return label;
    }

    private KrPanel createListView() {
        KrPanel wrapper = new KrPanel(new KrBorderLayout(4, 0));

        KrLabel label = new KrLabel("List View");
        label.setForeground(lightGray);
        label.setName("list_view.label");

        List<String> itemValues = new ArrayList<>(15);
        for (int i = 0; i < 15; ++i) {
            itemValues.add("Item " + i);
        }
        KrListItemModel<String> model = new KrListItemModel<>(itemValues);
        KrListView listView = new KrListView(model);
        listView.getSelectionModel().addSelectionListener((oldSelection, newSelection) ->
                System.out.println("newSelection = " + newSelection));
        listView.addDoubleClickListener(itemIndex -> System.out.println("2x clicked itemIndex: " + itemIndex));

        wrapper.add(label, NORTH);
        wrapper.add(listView, CENTER);
        wrapper.setPreferredHeight(120);
        return wrapper;
    }

    private KrPanel createTableView() {
        KrPanel wrapper = new KrPanel(new KrBorderLayout(4, 0));

        KrLabel label = new KrLabel("Table View");
        label.setForeground(lightGray);
        label.setName("list_view.label");

        KrTableView.KrTableColumnModel columnModel = new KrTableView.KrTableColumnModel() {
            private List<String> columns = Arrays.asList("Col 0", "Col 1", "Col 2");

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public String getColumnName(int index) {
                return columns.get(index);
            }

            @Override
            public int getModelIndex(int index) {
                return index;
            }
        };

        KrItemModel model = new KrItemModel() {
            private String[][] values = new String[20][3];

            {
                for (int i = 0; i < 20; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        values[i][j] = "val " + i + ":" + j;
                    }
                }
            }

            @Override
            public int getRowCount() {
                return 20;
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValue(int row, int column, KrModelIndex parent) {
                return values[row][column];
            }
        };

        KrTableView table = new KrTableView(model);
        table.setColumnModel(columnModel);

        wrapper.add(label, NORTH);
        wrapper.add(table, CENTER);
        wrapper.setPreferredHeight(120);

        return wrapper;
    }

    private KrPanel createCheckbox() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Checkboxes");
        label.setForeground(lightGray);
        label.setName("buttons.label");
        label.setGeometry(0, 0, 60, 20);

        KrCheckbox checkboxA = new KrCheckbox();
        checkboxA.setText("Extra topping!");
        checkboxA.setPosition(10, 20);
        checkboxA.setChecked(true);
        checkboxA.setSize(checkboxA.getPreferredSize());

        wrapper.add(label);
        wrapper.add(checkboxA);

        wrapper.setPreferredHeight(50);
        return wrapper;
    }

    private KrWidget createButtons() {
        KrLabel label = new KrLabel("Buttons");
        label.setForeground(lightGray);
        label.setName("buttons.label");
        label.setGeometry(0, 0, 100, 20);

        KrButton button = new KrButton("");
        button.setName("buttons.button");
        button.setGeometry(0, 20, 26, 26);
        button.setIcon(new KrFontAwesomeIcon(KrFontAwesomeGlyph.BARS));
        button.addMouseListener(new KrMouseListener.KrMouseAdapter() {
            @Override
            public void mouseDoubleClicked(KrMouseEvent event) {
                System.out.println("event = " + event);
            }
        });

        final KrMenu menu = createMenu();

        button.addListener(() -> menu.showAt(getDefaultToolkit().getInputSource().getMousePosition()));

        KrToggleButton toggleButton = new KrToggleButton("Toggle Button");
        toggleButton.setName("buttons.toggleButton");
        toggleButton.setGeometry(0, 50, 100, 26);
        toggleButton.setIcon(new KrFontAwesomeIcon(KrFontAwesomeGlyph.PLUS));
        toggleButton.addToggleListener(active -> {
            if (active) {
                canvas.getTooltipManager().showCustomTooltip(createDummyContent("Dummy tooltip"));
            } else {
                canvas.getTooltipManager().stopShowingCustomTooltip();
            }
        });

        KrLabel groupLabel = new KrLabel("Toggle Button Group");
        groupLabel.setForeground(lightGray);
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
        buttonGroup.setAllowUnCheck(false);

        KrPanel panel = new KrPanel();
        panel.setName("buttons.container");
        panel.setLayout(new KrAbsoluteLayout());
        panel.add(label);
        panel.add(button);
        panel.add(toggleButton);
        panel.add(groupLabel);
        panel.add(buttonGroup);
        panel.setPreferredHeight(140);

        return panel;
    }

    private KrWidget createSplitPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Split Panel");
        label.setForeground(lightGray);
        label.setGeometry(0, 0, 250, 20);

        KrSplitPanel splitPanel = new KrSplitPanel();
        splitPanel.add(createDummyContent("Top Panel"), new KrUnifiedSize(60, 1));
        splitPanel.add(createDummyContent("Middle Panel"), new KrUnifiedSize(60, 1));
        splitPanel.add(createDummyContent("Bottom Panel"), new KrUnifiedSize(60, 1));

        splitPanel.setGeometry(0, 20, 250, 300);

        wrapper.add(label);
        wrapper.add(splitPanel);
        wrapper.setPreferredHeight(320);

        return wrapper;
    }

    private KrWidget createScrollPanel() {
        KrPanel wrapper = new KrPanel(new KrBorderLayout(4, 0));

        KrLabel label = new KrLabel("Scroll Panel");
        label.setForeground(lightGray);

        KrWidget panel = createDummyContent("DUMMY");
        panel.setPreferredSize(new Vector2(120, 200));
        KrScrollPanel scrollPanel = new KrScrollPanel(panel);

        wrapper.add(label, NORTH);
        wrapper.add(scrollPanel, CENTER);
        wrapper.setPreferredHeight(120);

        return wrapper;
    }

    private KrPanel createScrollBarsPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Scroll Bars");
        label.setForeground(lightGray);
        label.setGeometry(0, 0, 150, 20);

        KrPanel verticalScrollBarPanel = createVerticalScrollBarPanel();
        KrPanel horizontalScrollBarPanel = createHorizontalScrollBarPanel();

        wrapper.add(label);
        wrapper.add(verticalScrollBarPanel);
        wrapper.add(horizontalScrollBarPanel);

        wrapper.setPreferredHeight(120);

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
        label.setForeground(lightGray);
        label.setGeometry(0, 0, 180, 20);

        KrPanel panel = new KrPanel();
        panel.setLayout(new KrFlowLayout(HORIZONTAL, 5, 5));
        panel.setBackground(darkGray);

        KrLabel labelA = new KrLabel("Label A");
        labelA.setBackground(darkerGray);
        labelA.setName("flowlayoutH.labelA");

        KrLabel labelB = new KrLabel("Some very long label");
        labelB.setBackground(darkerGray);
        labelB.setName("flowlayoutH.labelB");

        KrLabel labelC = new KrLabel("X");
        labelC.setBackground(darkerGray);
        labelC.setName("flowlayoutH.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);
        panel.setGeometry(0, 20, 180, 40);

        wrapper.add(label);
        wrapper.add(panel);
        wrapper.setPreferredHeight(60);
        return wrapper;
    }

    private KrWidget createVerticalFlowLayoutPanel() {
        KrPanel wrapper = new KrPanel();

        KrLabel label = new KrLabel("Vertical Flow Layout");
        label.setForeground(lightGray);
        label.setGeometry(0, 0, 180, 20);

        KrWidget panel = new KrPanel();
        panel.setBackground(darkGray);

        panel.setLayout(new KrFlowLayout(VERTICAL, 5, 5));

        KrLabel labelA = new KrLabel("First Row");
        labelA.setBackground(darkerGray);
        labelA.setName("flowlayoutV.labelA");

        KrLabel labelB = new KrLabel("Second Row");
        labelB.setBackground(darkerGray);
        labelB.setName("flowlayoutV.labelB");

        KrLabel labelC = new KrLabel("Third Row");
        labelC.setBackground(darkerGray);
        labelC.setName("flowlayoutV.labelC");

        panel.add(labelA);
        panel.add(labelB);
        panel.add(labelC);
        panel.setGeometry(0, 20, 180, 120);

        wrapper.add(label);
        wrapper.add(panel);
        wrapper.setPreferredHeight(140);

        return wrapper;
    }

    private KrWidget createBorderLayoutPanel() {
        KrPanel wrapper = new KrPanel(new KrBorderLayout(4, 0));

        KrLabel label = new KrLabel("Border Layout");
        label.setForeground(lightGray);
        label.setGeometry(0, 0, 180, 20);

        KrWidget panel = new KrPanel();
        panel.setBackground(darkGray);
        panel.setLayout(new KrBorderLayout(4, 4));

        KrLabel topWidget = new KrLabel("Top");
        topWidget.setName("borderlayout.top");
        topWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        topWidget.setBackground(darkerGray);
        topWidget.setPreferredSize(new Vector2(100, 40));

        KrLabel bottomWidget = new KrLabel("Bottom");
        bottomWidget.setName("borderlayout.bottom");
        bottomWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        bottomWidget.setBackground(darkerGray);
        bottomWidget.setPreferredSize(new Vector2(100, 20));

        KrLabel leftWidget = new KrLabel("W");
        leftWidget.setName("borderlayout.left");
        leftWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        leftWidget.setBackground(darkerGray);
        leftWidget.setPreferredSize(new Vector2(30, 20));

        KrLabel rightWidget = new KrLabel("E");
        rightWidget.setName("borderlayout.right");
        rightWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        rightWidget.setBackground(darkerGray);
        rightWidget.setPreferredSize(new Vector2(20, 20));

        KrLabel centerWidget = new KrLabel("CENTER");
        centerWidget.setTextAlignment(KrAlignment.MIDDLE_CENTER);
        centerWidget.setName("borderlayout.center");
        centerWidget.setBackground(darkerGray);
        centerWidget.setPreferredSize(new Vector2(100, 20));

        panel.add(topWidget, NORTH);
        panel.add(bottomWidget, KrBorderLayout.Constraint.SOUTH);
        panel.add(leftWidget, KrBorderLayout.Constraint.WEST);
        panel.add(rightWidget, KrBorderLayout.Constraint.EAST);
        panel.add(centerWidget, KrBorderLayout.Constraint.CENTER);

        panel.setGeometry(0, 20, 180, 120);

        wrapper.add(label, KrBorderLayout.Constraint.NORTH);
        wrapper.add(panel, KrBorderLayout.Constraint.CENTER);
        wrapper.setPreferredHeight(140);

        return wrapper;
    }

    private KrWidget createGridLayout() {
        KrLabel formLabel = new KrLabel("Grid Layout / Text Field / Spinner");
        formLabel.setForeground(lightGray);
        formLabel.setName("grid_layout.form_label");
        formLabel.setPosition(0, 0);

        KrWidget usernameLabel = new KrLabel("Username");
        usernameLabel.setName("grid_layout.label.username");

        KrWidget usernameEdit = new KrTextField();
        usernameEdit.setName("grid_layout.textfield.username");

        KrWidget weight = new KrLabel("Weight");
        weight.setName("grid_layout.label.weight");
        KrSpinner weightEdit = new KrSpinner();
        weightEdit.setName("grid_layout.spinner.weight");

        KrLabel genderLabel = new KrLabel("Gender");
        genderLabel.setName("grid_layout.label.gender");
        KrComboBox<String> genderEditor = new KrComboBox<>();
        genderEditor.setValues(Arrays.asList("Male", "Female", "Other", "Pretentious"));

        KrPanel fields = new KrPanel();
        KrGridLayout formLayout = new KrGridLayout(2, 5, 3);
        formLayout.setColumnSizePolicy(new KrSizePolicyModel(new KrUnifiedSize(55, 0), new KrUnifiedSize(50, 1)));
        fields.setLayout(formLayout);
        fields.add(usernameLabel, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        fields.add(usernameEdit, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        fields.add(weight, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        fields.add(weightEdit, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));
        fields.add(genderLabel, new Constraint(KrAlignment.MIDDLE_RIGHT, false, false));
        fields.add(genderEditor, new Constraint(KrAlignment.MIDDLE_LEFT, true, false));

        KrPanel form = new KrPanel(new KrFlowLayout(VERTICAL));
        form.add(formLabel);
        form.add(fields);
        form.setPreferredHeight(100);

        return form;
    }

    public KrPanel createDummyContent(String text) {
        KrPanel panel = new KrPanel() {
            @Override
            protected void drawSelf(KrRenderer renderer) {
                renderer.setBrush(getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER));
                renderer.fillRoundedRect(0, 0, getWidth(), getHeight(), 2);

                renderer.setBrush(getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND_LIGHT));
                renderer.fillRoundedRect(1, 1, getWidth() - 2, getHeight() - 2, 2);
            }
        };
        panel.setLayout(new KrBorderLayout());
        panel.add(new KrLabel(text), CENTER);
        return panel;
    }

    @Override
    public void render() {
        fpsLogger.log();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getRawDeltaTime();
        getDefaultToolkit().update(deltaTime);
        canvas.draw();
    }

    @Override
    public void resize(int width, int height) {
        gl.glViewport(0, 0, width, height);
        canvas.setSize(width, height);
    }

    /**
     * A container panel that lays out it's children in 4 columns.
     */
    private class KrDemoPanel extends KrPanel {

        private static final int COLUMN_COUNT = 4;

        private KrPanel[] columns = new KrPanel[COLUMN_COUNT];

        public KrDemoPanel() {
            KrGridLayout layout = new KrGridLayout(COLUMN_COUNT, 4, 4);
            setLayout(layout);
            setName("demo_panel");
            for (int i = 0; i < COLUMN_COUNT; ++i) {
                KrPanel column = new KrPanel(new KrFlowLayout(KrOrientation.VERTICAL, 4, 4));
                column.setName("demo_panel.column_" + i);
                column.setPreferredSize(2000, 2000);

                columns[i] = column;
                add(column);
            }
        }

        public void addChild(KrWidget child, int columnId) {
            if (columnId >= COLUMN_COUNT) {
                throw new IllegalArgumentException("Column id out of bounds.");
            }
            columns[columnId].add(child);
        }
    }
}
