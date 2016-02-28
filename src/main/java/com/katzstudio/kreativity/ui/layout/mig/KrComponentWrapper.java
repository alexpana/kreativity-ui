package com.katzstudio.kreativity.ui.layout.mig;
/*
 * License (BSD):
 * ==============
 *
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * @version 1.0
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: 2006-sep-08
 */

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrSpinner;
import com.katzstudio.kreativity.ui.component.KrTextField;
import com.katzstudio.kreativity.ui.component.KrWidget;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;

import java.lang.reflect.Method;

/**
 */
public class KrComponentWrapper implements ComponentWrapper {
    private static boolean maxSet = false;

    private static boolean vp = true;

    private final KrWidget c;
    private int compType = TYPE_UNSET;
    private Boolean bl = null;
    private boolean prefCalled = false;

    public KrComponentWrapper(KrWidget c) {
        this.c = c;
    }

    @Override
    public final int getBaseline(int width, int height) {
        if (BL_METHOD == null)
            return -1;

        try {
            Object[] args = new Object[]{
                    width < 0 ? c.getWidth() : width,
                    height < 0 ? c.getHeight() : height
            };

            return (Integer) BL_METHOD.invoke(c, args);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public final Object getComponent() {
        return c;
    }

    /**
     * Cache.
     */
//    private final static IdentityHashMap<FontMetrics, Point.Float> FM_MAP = new IdentityHashMap<>(4);
//    private final static Font SUBST_FONT = new Font("sansserif", Font.PLAIN, 11);
    @Override
    public final float getPixelUnitFactor(boolean isHor) {
//        switch (PlatformDefaults.getLogicalPixelBase()) {
//            case PlatformDefaults.BASE_FONT_SIZE:
//                Font font = c.getFont();
//                FontMetrics fm = c.getFontMetrics(font != null ? font : SUBST_FONT);
//                Point.Float p = FM_MAP.get(fm);
//                if (p == null) {
//                    Rectangle2D r = fm.getStringBounds("X", c.getGraphics());
//                    p = new Point.Float(((float) r.getWidth()) / 6f, ((float) r.getHeight()) / 13.27734375f);
//                    FM_MAP.put(fm, p);
//                }
//                return isHor ? p.x : p.y;
//
//            case PlatformDefaults.BASE_SCALE_FACTOR:
//
//                Float s = isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
//                if (s != null)
//                    return s;
//                return (isHor ? getHorizontalScreenDPI() : getVerticalScreenDPI()) / (float) PlatformDefaults.getDefaultDPI();
//
//            default:
//                return 1f;
//        }
        return 1;
    }

//	/** Cache.
//	 */
//	private final static IdentityHashMap<FontMetrics, Point.Float> FM_MAP2 = new IdentityHashMap<FontMetrics, Point.Float>(4);
//	private final static Font SUBST_FONT2 = new Font("sansserif", Font.PLAIN, 11);
//
//	public float getDialogUnit(boolean isHor)
//	{
//		Font font = c.getFont();
//		FontMetrics fm = c.getFontMetrics(font != null ? font : SUBST_FONT2);
//		Point.Float dluP = FM_MAP2.get(fm);
//		if (dluP == null) {
//			float w = fm.charWidth('X') / 4f;
//			int ascent = fm.getAscent();
//			float h = (ascent > 14 ? ascent : ascent + (15 - ascent) / 3) / 8f;
//
//			dluP = new Point.Float(w, h);
//			FM_MAP2.put(fm, dluP);
//		}
//		return isHor ? dluP.x : dluP.y;
//	}

    @Override
    public final int getX() {
        return (int) c.getX();
    }

    @Override
    public final int getY() {
        return (int) c.getY();
    }

    @Override
    public final int getHeight() {
        return (int) c.getHeight();
    }

    @Override
    public final int getWidth() {
        return (int) c.getWidth();
    }

    @Override
    public final int getScreenLocationX() {
        Vector2 p = new Vector2();
        KrCanvas.convertPointToScreen(p, c);
        return (int) p.x;
    }

    @Override
    public final int getScreenLocationY() {
        Vector2 p = new Vector2();
        KrCanvas.convertPointToScreen(p, c);
        return (int) p.y;
    }

    @Override
    public final int getMinimumHeight(int sz) {
        if (!prefCalled) {
            c.getPreferredSize(); // To defeat a bug where the minimum size is different before and after the first call to getPreferredSize();
            prefCalled = true;
        }
        return (int) c.getMinSize().y;
    }

    @Override
    public final int getMinimumWidth(int sz) {
        if (!prefCalled) {
            c.getPreferredSize(); // To defeat a bug where the minimum size is different before and after the first call to getPreferredSize();
            prefCalled = true;
        }
        return (int) c.getMinSize().x;
    }

    @Override
    public final int getPreferredHeight(int sz) {
        // If the component has not gotten size yet and there is a size hint, trick Swing to return a better height.
        if (c.getWidth() == 0 && c.getHeight() == 0 && sz != -1)
            c.setBounds(c.getX(), c.getY(), sz, 1);

        return (int) c.getPreferredSize().y;
    }

    @Override
    public final int getPreferredWidth(int sz) {
        // If the component has not gotten size yet and there is a size hint, trick Swing to return a better height.
        if (c.getWidth() == 0 && c.getHeight() == 0 && sz != -1)
            c.setBounds(c.getX(), c.getY(), 1, sz);

        return (int) c.getPreferredSize().x;
    }

    @Override
    public final int getMaximumHeight(int sz) {
        if (!isMaxSet(c))
            return Short.MAX_VALUE;

        return (int) c.getMaxSize().y;
    }

    @Override
    public final int getMaximumWidth(int sz) {
        if (!isMaxSet(c))
            return Short.MAX_VALUE;

        return (int) c.getMaxSize().x;
    }


    private boolean isMaxSet(KrWidget c) {
//        if (IMS_METHOD != null) {
//            try {
//                return (Boolean) IMS_METHOD.invoke(c, (Object[]) null);
//            } catch (Exception e) {
//                IMS_METHOD = null;  // So we do not try every time.
//            }
//        }
        return isMaxSizeSetOn1_4();
    }

    @Override
    public final ContainerWrapper getParent() {
        KrWidget p = c.getParent();
        return p != null ? new KrContainerWrapper(p) : null;
    }

    @Override
    public final int getHorizontalScreenDPI() {
        return PlatformDefaults.getDefaultDPI();
    }

    @Override
    public final int getVerticalScreenDPI() {
        return PlatformDefaults.getDefaultDPI();
    }

    @Override
    public final int getScreenWidth() {
        return (int) c.getCanvas().getWidth();
    }

    @Override
    public final int getScreenHeight() {
        return (int) c.getCanvas().getHeight();
    }

    @Override
    public final boolean hasBaseline() {
        if (bl == null) {
            try {
                if (BL_RES_METHOD == null || BL_RES_METHOD.invoke(c).toString().equals("OTHER")) {
                    bl = Boolean.FALSE;
                } else {
                    Vector2 d = c.getMinSize();
                    bl = getBaseline((int) d.x, (int) d.y) > -1;
                }
            } catch (Throwable ex) {
                bl = Boolean.FALSE;
            }
        }
        return bl;
    }

    @Override
    public final String getLinkId() {
        return c.getName();
    }

    @Override
    public final void setBounds(int x, int y, int width, int height) {
        c.setBounds(x, y, width, height);
    }

    @Override
    public boolean isVisible() {
        return c.isVisible();
    }

    @Override
    public final int[] getVisualPadding() {
//        if (vp && c instanceof JTabbedPane) {
//            if (UIManager.getLookAndFeel().getClass().getName().endsWith("WindowsLookAndFeel"))
//                return new int[]{-1, 0, 2, 2};
//        }

        return null;
    }

    public static boolean isMaxSizeSetOn1_4() {
        return maxSet;
    }

    public static void setMaxSizeSetOn1_4(boolean b) {
        maxSet = b;
    }

    public static boolean isVisualPaddingEnabled() {
        return vp;
    }

    public static void setVisualPaddingEnabled(boolean b) {
        vp = b;
    }

    @Override
    public final void paintDebugOutline() {
//        if (!c.isShowing())
//            return;
//
//        Graphics2D g = (Graphics2D) c.getGraphics();
//        if (g == null)
//            return;
//
//        g.setPaint(DB_COMP_OUTLINE);
//        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10f, new float[]{2f, 4f}, 0));
//        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public int getComponetType(boolean disregardScrollPane) {
        if (compType == TYPE_UNSET)
            compType = checkType(disregardScrollPane);

        return compType;
    }

    @Override
    public int getLayoutHashCode() {
        Vector2 d = c.getMaxSize();
        int hash = (int) d.x + ((int) d.y << 5);

        d = c.getPreferredSize();
        hash += ((int) d.x << 10) + ((int) d.y << 15);

        d = c.getMinSize();
        hash += ((int) d.x << 20) + ((int) d.y << 25);

        if (c.isVisible())
            hash += 1324511;

        String id = getLinkId();
        if (id != null)
            hash += id.hashCode();

        return hash;
    }

    private int checkType(boolean disregardScrollPane) {
        KrWidget c = this.c;

        // TODO(alex): fix this after implementing scroll panels
//        if (disregardScrollPane) {
//            if (c instanceof KrScrollPanel) {
//                c = ((JScrollPane) c).getViewport().getView();
//            } else if (c instanceof ScrollPane) {
//                c = ((ScrollPane) c).getComponent(0);
//            }
//        }

        if (c instanceof KrSpinner) {
            return TYPE_SPINNER;
        } else if (c instanceof KrTextField) {
            return TYPE_TEXT_FIELD;
        } else if (c instanceof KrLabel) {
            return TYPE_LABEL;
        } else if (c instanceof KrCheckbox) {
            return TYPE_CHECK_BOX;
        } else if (c instanceof KrButton) {
            return TYPE_BUTTON;
//        } else if (c instanceof JComboBox || c instanceof Choice) {
//            return TYPE_LABEL;
//        } else if (c instanceof JTextComponent || c instanceof TextComponent) {
//            return TYPE_TEXT_AREA;
        } else if (c instanceof KrPanel) {
            return TYPE_PANEL;
//        } else if (c instanceof JList || c instanceof List) {
//            return TYPE_LIST;
//        } else if (c instanceof JTable) {
//            return TYPE_TABLE;
//        } else if (c instanceof JSeparator) {
//            return TYPE_SEPARATOR;
//        } else if (c instanceof JSpinner) {
//            return TYPE_SPINNER;
//        } else if (c instanceof JProgressBar) {
//            return TYPE_PROGRESS_BAR;
//        } else if (c instanceof JSlider) {
//            return TYPE_SLIDER;
//        } else if (c instanceof JScrollPane) {
//            return TYPE_SCROLL_PANE;
//        } else if (c instanceof JScrollBar || c instanceof Scrollbar) {
//            return TYPE_SCROLL_BAR;
//        } else if (c instanceof Container) {    // only AWT components is not containers.
//            return TYPE_CONTAINER;
        }
        return TYPE_UNKNOWN;
    }

    @Override
    public final int hashCode() {
        return getComponent().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof ComponentWrapper && getComponent().equals(((ComponentWrapper) o).getComponent());

    }

    /**
     * Cached method used for getting base line with reflection.
     */
    private static Method BL_METHOD = null;
    private static Method BL_RES_METHOD = null;
}
