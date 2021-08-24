package fr.skytale;

import fr.skytale.ttfparser.TTFParser;
import fr.skytale.ttfparser.tables.TTFPoint;
import fr.skytale.ttfparser.tables.TTFTableManager;
import fr.skytale.ttfparser.TTFAlphabet;
import fr.skytale.ttfparser.TTFCharacter;
import fr.skytale.viewer.LetterCanvas;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URL fontURL = Main.class.getClassLoader().getResource("resources/KGEverSinceNewYork.ttf");
        TTFParser ttfParser = new TTFParser(fontURL);
        TTFAlphabet ttfAlphabet = ttfParser.parse();

        System.out.println(ttfParser.getTableManager().getTable(TTFTableManager.CMAP));

        char character = 'A';

        if(!ttfAlphabet.supportCharacter(character)) {
            throw new RuntimeException(String.format("Unsupported character '%c'.", character));
        }
        System.out.println(ttfAlphabet.getCharacter(character));

        JFrame frame = new JFrame("My Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LetterCanvas canvas = new LetterCanvas(ttfAlphabet.getCharacter(character));
        canvas.setSize(500, 500);
        frame.setTitle("Printed character: " + character);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if(!ttfAlphabet.supportCharacter(c)) {
                    System.out.println(String.format("Unsupported character '%c'.", c));
                    return;
                }
                TTFCharacter ttfCharacter = ttfAlphabet.getCharacter(c);

                List<TTFPoint> points = ttfCharacter.getGlyf().getPoints();
                List<Integer> contourEnds = ttfCharacter.getGlyf().getContourEnds();
                int index = 0;
                int contourEndIndex = 0;
                for (Point2D point : points) {
                    System.out.println(String.format("x=%.0f y=%.0f", point.getX(), point.getY()));
                    if (contourEnds.get(contourEndIndex) == index) {
                        System.out.println("----------");
                        contourEndIndex++;
                    }
                    index++;
                }

                System.out.println(ttfCharacter);
                canvas.setCharacter(ttfCharacter);
                frame.setTitle("Printed character: " + c);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

}
