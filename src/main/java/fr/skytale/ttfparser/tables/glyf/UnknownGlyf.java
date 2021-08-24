package fr.skytale.ttfparser.tables.glyf;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public class UnknownGlyf extends Glyf {

    public UnknownGlyf(short numberOfContours, short xMin, short yMin, short xMax, short yMax) {
        super(numberOfContours, xMin, yMin, xMax, yMax);
    }

    @Override
    public void loadPoints(SuperBufferedInputStream sbis) throws IOException {
        // There is nothing to implement here.
    }

    @Override
    public String toString() {
        return "UnknownGlyf{" +
                "numberOfContours=" + numberOfContours +
                ", xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                ", points=" + points +
                ", contourEnds=" + contourEnds +
                ", instructionLength=" + instructionLength +
                ", instructions=" + instructions +
                '}';
    }
}
