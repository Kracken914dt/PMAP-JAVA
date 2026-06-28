package com.pmap.desktop.view;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MateriaForm().setVisible(true));
    }
}
