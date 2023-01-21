package eu.darkbot.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.intellij.lang.annotations.MagicConstant;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;

@UtilityClass
public class Popups {

    @Getter @Setter // Called by the bot to initialize popup icon
    private static Image defaultIcon;

    public static Builder of(String title, Object message) {
        return new Builder().title(title).message(message);
    }

    public static Builder of(String title, Object message, @MagicConstant(intValues = {
            JOptionPane.ERROR_MESSAGE,
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.PLAIN_MESSAGE}) int messageType) {
        return new Builder().title(title).message(message).messageType(messageType);
    }

    public static Builder of(String title, JOptionPane pane) {
        return new Builder(pane).title(title);
    }

    @SuppressWarnings("PMD.TooManyMethods")
    public static class Builder {
        // Pane
        private Object message;
        private Icon icon;
        @MagicConstant(intValues = {
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.PLAIN_MESSAGE})
        private int messageType = JOptionPane.PLAIN_MESSAGE;
        @MagicConstant(intValues = {
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.OK_CANCEL_OPTION})
        private int optionType = JOptionPane.DEFAULT_OPTION;
        private Object[] options;
        private Object initialValue;
        private Border border;

        // The built pane
        private JOptionPane pane;

        // Dialog
        private Component parent;
        private String title = "DarkBot";
        private Image titleIcon = defaultIcon;
        private boolean showOnTop = true;
        private JButton defaultButton;
        private Consumer<JDialog> callback;

        public Builder() {
        }

        // Skip all pane building, straight use a built pane
        public Builder(JOptionPane pane) {
            this.pane = pane;
        }

        // Pane setters
        public Builder message(Object message) {
            ensureNonBuilt();
            this.message = message;
            return this;
        }

        public Builder icon(Icon icon) {
            ensureNonBuilt();
            this.icon = icon;
            return this;
        }

        public Builder messageType(@MagicConstant(intValues = {
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.PLAIN_MESSAGE}) int messageType) {
            ensureNonBuilt();
            this.messageType = messageType;
            return this;
        }

        public Builder optionType(@MagicConstant(intValues = {
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.OK_CANCEL_OPTION}) int optionType) {
            ensureNonBuilt();
            this.optionType = optionType;
            return this;
        }

        public Builder options(Object... options) {
            ensureNonBuilt();
            this.options = Arrays.copyOf(options, options.length);
            return this;
        }

        public Builder initialValue(Object initialValue) {
            ensureNonBuilt();
            this.initialValue = initialValue;
            return this;
        }

        public Builder border(Border border) {
            ensureNonBuilt();
            this.border = border;
            return this;
        }

        // Dialog setters
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleIcon(Image titleIcon) {
            this.titleIcon = titleIcon;
            return this;
        }

        public Builder parent(Component parent) {
            this.parent = parent;
            return this;
        }

        public Builder alwaysOnTop(boolean showOnTop) {
            this.showOnTop = showOnTop;
            return this;
        }

        public Builder defaultButton(JButton defaultButton) {
            this.defaultButton = defaultButton;
            return this;
        }

        public Builder callback(Consumer<JDialog> callback) {
            this.callback = callback;
            return this;
        }

        private void ensureNonBuilt() {
            if (pane != null)
                throw new UnsupportedOperationException("Pane may not be modified after built, or rebuilt.");
        }

        public JOptionPane build() {
            ensureNonBuilt();

            this.pane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
            if (border != null) pane.setBorder(border);
            return pane;
        }

        public Object showSync() {
            try {
                if (SwingUtilities.isEventDispatchThread()) {
                    show();
                } else {
                    SwingUtilities.invokeAndWait(this::show);
                }
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return pane.getValue();
        }

        public int showOptionSync() {
            Object selectedValue = showSync();
            if (selectedValue == null) return JOptionPane.CLOSED_OPTION;

            Object[] options = pane.getOptions();
            if (options == null) {
                if (selectedValue instanceof Integer) return (Integer) selectedValue;
                return JOptionPane.CLOSED_OPTION;
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(selectedValue)) return i;
            }
            return JOptionPane.CLOSED_OPTION;
        }

        public void showAsync() {
            SwingUtilities.invokeLater(this::show);
        }

        private void show() {
            if (pane == null) build();

            JDialog dialog = parent == null ? pane.createDialog(title) : pane.createDialog(parent, title);

            if (callback != null) callback.accept(dialog);
            if (defaultButton != null) dialog.getRootPane().setDefaultButton(defaultButton);
            dialog.setIconImage(titleIcon == null ? defaultIcon : titleIcon);
            dialog.setAlwaysOnTop(showOnTop);
            dialog.setVisible(true);
            // Once done displaying, dispose everything
            dialog.dispose();
        }

    }

}
