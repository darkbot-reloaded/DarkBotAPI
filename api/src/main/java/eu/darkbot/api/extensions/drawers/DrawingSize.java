package eu.darkbot.api.extensions.drawers;

public interface DrawingSize {
    int getWidth();
    int getHeight();
    int getWidthMiddle();
    int getHeightMiddle();

    double getScaleX();
    double getScaleY();

    double toScreenPointX(double gameX);
    double toScreenPointY(double gameY);

    double toScreenSizeW(double gameW);
    double toScreenSizeH(double gameH);

    double toGameLocationX(double screenX);
    double toGameLocationY(double screenY);

}
