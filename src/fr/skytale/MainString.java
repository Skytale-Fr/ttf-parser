package fr.skytale;

import fr.skytale.ttfparser.TTFParser;
import fr.skytale.ttfparser.tables.TTFTableManager;
import fr.skytale.ttfparser.TTFAlphabet;
import fr.skytale.viewer.StringCanvas;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainString {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URL fontURL = MainString.class.getClassLoader().getResource("resources/KGEverSinceNewYork.ttf");
        TTFParser ttfParser = new TTFParser(fontURL);
        TTFAlphabet ttfAlphabet = ttfParser.parse();

        System.out.println(ttfParser.getTableManager().getTable(TTFTableManager.CMAP));

        String message = "Coucou";

        if(!ttfAlphabet.supportString(message)) {
            throw new RuntimeException(String.format("Unsupported string '%s'.", message));
        }

        JFrame frame = new JFrame("My Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StringCanvas canvas = new StringCanvas(ttfAlphabet.getString(message));
        canvas.setSize(1500, 500);
        frame.setTitle("Printed string: " + message);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

}
