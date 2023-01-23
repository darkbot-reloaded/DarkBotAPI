package eu.darkbot.util.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class JTitledPopupMenuSeparator extends JPopupMenu.Separator {
    private static final long serialVersionUID = 1L;

    // Used exclusively for constant font data
    private static final JComponent MENU = new JPopupMenu();

    public JTitledPopupMenuSeparator(String title) {
        setBorder(new TitledBorder(title));
        setPreferredSize(new Dimension(0, MENU.getFontMetrics(MENU.getFont()).getHeight()));
    }

    private static class TitledBorder implements Border {

        private final String title;

        public TitledBorder(String title) {
            this.title = title;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int height = c.getFontMetrics(MENU.getFont()).getHeight();
            return new Insets(height, 0, 0, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            FontMetrics fm = c.getFontMetrics(MENU.getFont());
            int titleWidth = fm.stringWidth(title);
            int titleHeight = fm.getHeight();

            // fill background
            g.setColor(MENU.getBackground());
            g.fillRect(x, y, width, titleHeight);

            int gap = 4;

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIManager.getColor("Label.disabledForeground"));

                // paint separator lines
                int sepWidth = (width - titleWidth) / 2 - gap - gap;
                if (sepWidth > 0) {
                    int sy = y + Math.round(titleHeight / 2f);
                    float sepHeight = 1f;

                    g2.fill(new Rectangle2D.Float(x + gap, sy, sepWidth, sepHeight));
                    g2.fill(new Rectangle2D.Float(x + width - gap - sepWidth, sy, sepWidth, sepHeight));
                }

                // draw title
                int xt = x + ((width - titleWidth) / 2);
                int yt = y + fm.getAscent();

                g2.drawString(title, xt, yt);
            } finally {
                g2.dispose();
            }
        }
    }
}
