package org.raven;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.raven.utils.ThemesUtils;

import javax.swing.*;
import java.awt.*;

public class DemoFrame extends JFrame {

    public DemoFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        createMenuBar();
        ThemesUtils.initThemes();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuThemes = new JMenu("Themes");
        JMenuItem menuExit = new JMenuItem("Exit");
        ButtonGroup group = new ButtonGroup();
        JCheckBoxMenuItem flatLafLight = createItem(new ThemesInfo("FlatLaf Light", FlatLightLaf.class.getName()));
        JCheckBoxMenuItem flatLafDark = createItem(new ThemesInfo("FlatLaf Dark", FlatDarkLaf.class.getName()));
        JCheckBoxMenuItem flatLafIntelliJ = createItem(new ThemesInfo("FlatLaf IntelliJ", FlatIntelliJLaf.class.getName()));
        JCheckBoxMenuItem flatLafDarcula = createItem(new ThemesInfo("FlatLaf Darcula", FlatDarculaLaf.class.getName()));
        JCheckBoxMenuItem flatLafMacLight = createItem(new ThemesInfo("FlatLaf macOS Light", FlatMacLightLaf.class.getName()));
        JCheckBoxMenuItem flatLafMacDark = createItem(new ThemesInfo("FlatLaf macOS Dark", FlatMacDarkLaf.class.getName()));
        group.add(flatLafLight);
        group.add(flatLafDark);
        group.add(flatLafIntelliJ);
        group.add(flatLafDarcula);
        group.add(flatLafMacLight);
        group.add(flatLafMacDark);


        flatLafDarcula.setSelected(true);
        menuExit.addActionListener(e -> System.exit(0));

        menuThemes.add(flatLafLight);
        menuThemes.add(flatLafDark);
        menuThemes.add(flatLafIntelliJ);
        menuThemes.add(flatLafDarcula);
        menuThemes.add(flatLafMacLight);
        menuThemes.add(flatLafMacDark);

        menuFile.add(menuExit);
        menuBar.add(menuFile);
        menuBar.add(menuThemes);
        setJMenuBar(menuBar);
    }

    private JCheckBoxMenuItem createItem(ThemesInfo theme) {
        JCheckBoxMenuItem menu = new JCheckBoxMenuItem(theme.name);
        menu.addActionListener(e -> {
            if (menu.isSelected()) {
                changeTheme(theme);
            }
        });
        return menu;
    }

    private void changeTheme(ThemesInfo themeInfo) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            try {
                System.out.println("Change");
                UIManager.setLookAndFeel(themeInfo.lafClassName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    private class ThemesInfo {

        public ThemesInfo(String name, String lafClassName) {
            this.name = name;
            this.lafClassName = lafClassName;
        }

        protected String name;
        protected String lafClassName;
    }
}
