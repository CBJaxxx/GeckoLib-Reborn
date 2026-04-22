package net.nerdypuzzle.geckolib.parts;

import javax.swing.*;
import java.awt.*;

/**
 * Compatibility layer for PanelUtils methods that may not exist in all MCreator versions.
 * All methods are implemented directly to avoid dependency on MCreator's PanelUtils.
 */
public class PluginPanelUtils {

    /**
     * Compatible version of PanelUtils.northAndCenterElement(Component, Component)
     */
    public static JPanel northAndCenterElement(Component top, Component center) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add("North", top);
        p.add("Center", center);
        return p;
    }

    /**
     * Compatible version of PanelUtils.northAndCenterElement(Component, Component, int, int)
     */
    public static JPanel northAndCenterElement(Component top, Component center, int px, int py) {
        JPanel p = new JPanel(new BorderLayout(px, py));
        p.setOpaque(false);
        p.add("North", top);
        p.add("Center", center);
        return p;
    }

    /**
     * Compatible version of PanelUtils.centerAndSouthElement(Component, Component)
     */
    public static JPanel centerAndSouthElement(Component top, Component center) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add("Center", top);
        p.add("South", center);
        return p;
    }

    /**
     * Compatible version of PanelUtils.centerAndSouthElement(Component, Component, int, int)
     */
    public static JPanel centerAndSouthElement(Component top, Component center, int hg, int vg) {
        JPanel p = new JPanel(new BorderLayout(hg, vg));
        p.setOpaque(false);
        p.add("Center", top);
        p.add("South", center);
        return p;
    }

    /**
     * Wrapper for PanelUtils.join(Component...) - always available
     */
    public static JPanel join(Component... c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        p.setOpaque(false);
        for (Component comp : c) {
            if (comp != null) p.add(comp);
        }
        return p;
    }

    /**
     * Wrapper for PanelUtils.join(int, Component...) - always available
     */
    public static JPanel join(int align, Component... c) {
        JPanel p = new JPanel(new FlowLayout(align, 2, 2));
        p.setOpaque(false);
        for (Component comp : c) {
            if (comp != null) p.add(comp);
        }
        return p;
    }

    /**
     * Wrapper for PanelUtils.join(int, int, int, Component...) - always available
     */
    public static JPanel join(int align, int hgap, int vgap, Component... c) {
        JPanel p = new JPanel(new FlowLayout(align, hgap, vgap));
        p.setOpaque(false);
        for (Component comp : c) {
            if (comp != null) p.add(comp);
        }
        return p;
    }

    /**
     * Wrapper for PanelUtils.centerInPanel(Component) - always available
     */
    public static JPanel centerInPanel(Component component) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(component);
        return p;
    }

    /**
     * Wrapper for PanelUtils.totalCenterInPanel(Component) - always available
     */
    public static JPanel totalCenterInPanel(Component component) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(component);
        return p;
    }

    /**
     * Wrapper for PanelUtils.pullElementUp(Component) - always available
     */
    public static JPanel pullElementUp(Component element) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add("North", element);
        return p;
    }

    /**
     * Wrapper for PanelUtils.westAndEastElement(Component, Component) - always available
     */
    public static JPanel westAndEastElement(Component west, Component east) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add("West", west);
        p.add("East", east);
        return p;
    }

    /**
     * Wrapper for PanelUtils.westAndCenterElement(Component, Component) - always available
     */
    public static JPanel westAndCenterElement(Component west, Component center) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add("West", west);
        p.add("Center", center);
        return p;
    }

    /**
     * Compatible version of PanelUtils.westAndCenterElement(JComponent, JComponent, int, int)
     */
    public static JPanel westAndCenterElement(Component west, Component center, int px, int py) {
        JPanel p = new JPanel(new BorderLayout(px, py));
        p.setOpaque(false);
        p.add("West", west);
        p.add("Center", center);
        return p;
    }

    /**
     * Compatible version of PanelUtils.centerAndEastElement(Component, Component, int, int)
     */
    public static JPanel centerAndEastElement(Component center, Component east, int px, int py) {
        JPanel p = new JPanel(new BorderLayout(px, py));
        p.setOpaque(false);
        p.add("Center", center);
        p.add("East", east);
        return p;
    }

    /**
     * Compatible version of PanelUtils.centerInPanelPadding(Component, int, int)
     */
    public static JPanel centerInPanelPadding(Component component, int x, int y) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, x, y));
        p.setOpaque(false);
        p.add(component);
        return p;
    }
}
