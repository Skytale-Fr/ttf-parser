package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tables documentation:
 * Windows: [Table TAB in the left menu] https://docs.microsoft.com/en-us/typography/opentype/spec/
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6.html
 */
public class TTFTableManager {

    // Font Header Table
    public static final TTFTableID<TTFHeadTable> HEAD = new TTFTableID<>("head");
    // Horizontal Header Table
    public static final TTFTableID<TTFHheaTable> HHEA = new TTFTableID<>("hhea");
    // Horizontal Metrics Table
    public static final TTFTableID<TTFHmtxTable> HMTX = new TTFTableID<>("hmtx");
    // Maximum Profile
    public static final TTFTableID<TTFMaxpTable> MAXP = new TTFTableID<>("maxp");
    // Index to Location
    public static final TTFTableID<TTFLocaTable> LOCA = new TTFTableID<>("loca");
    // Glyph Data
    public static final TTFTableID<TTFGlyfTable> GLYF = new TTFTableID<>("glyf");
    // Character to Glyph Index Mapping Table
    public static final TTFTableID<TTFCmapTable> CMAP = new TTFTableID<>("cmap");

    private Map<TTFTableID<?>, TTFTable> tables = new HashMap<>();

    public void createTable(SuperBufferedInputStream sbis) throws IOException {
        TTFTable table = TTFTableFactory.createTable(sbis, this);
        TTFTableID<?> tableID = TTFTableFactory.getTableID(table);
        tables.put(tableID, table);
    }

    public void parseAttributes(SuperBufferedInputStream sbis) throws IOException {
        Collection<TTFTable> tableCollection = tables.values();
        List<TTFTable> sortedTables = tableCollection.stream().sorted(new Comparator<TTFTable>() {
            @Override
            public int compare(TTFTable table1, TTFTable table2) {
                return table1.getOffset() - table2.getOffset();
            }
        }).collect(Collectors.toList());


        List<TTFTable> priorityTable = Arrays.asList(
                getTable(HEAD),
                getTable(MAXP),
                getTable(HHEA),
                getTable(HMTX),
                getTable(LOCA),
                getTable(GLYF),
                getTable(CMAP)
        );
        sortedTables.removeAll(priorityTable);

        for(TTFTable table : priorityTable) {
            System.out.println(table.getTag());
            table.parseAttributes(sbis, this);
        }

        for(TTFTable table : sortedTables) {
            System.out.println(table.getTag());
            table.parseAttributes(sbis, this);
        }
    }

    public <T extends TTFTable> T getTable(TTFTableID<T> tableID) {
        TTFTable table = tables.get(tableID);
        if(table == null) return null;
        return (T) table;
    }


}
