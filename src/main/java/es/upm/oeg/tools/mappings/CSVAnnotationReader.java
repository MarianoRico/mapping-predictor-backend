package es.upm.oeg.tools.mappings;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVAnnotationReader implements AnnotationReader {

    private Path csvPath;
    private List<String> lines = new ArrayList<>();
    private Map<String, Integer> fileMap;

    // Lang in ISO 639-1 ("es") or if not exists, on ISO-631-2 format (eg: "ast")
    private String langA;
    private String langB;

    public CSVAnnotationReader(String filepath, String langA, String langB) throws IOException {
        csvPath = FileSystems.getDefault().getPath(filepath);
        BufferedReader reader = Files.newBufferedReader(csvPath);
        reader.lines().forEach(l -> lines.add(l));
        fileMap = getMap();

        this.langA = langA;
        this.langB = langB;
    }

    public Annotation getAnnotation(int id) {
        String linea = lines.get(id);
        return parseAnnotation(linea);
    }

    private Map<String, Integer> getMap() {
        // Obtiene el mapping a través de la primera línea
        String primera = lines.get(0);
        String[] campos = primera.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < campos.length; i++) {
            map.put(campos[i].trim().toLowerCase(), i);
        }
        return map;
    }

    private boolean parseFieldBool(String fieldName, String[] line) {
        String feature = parseFieldString(fieldName, line);
        long field = Long.parseLong(feature);
        if (field == 1)
            return true;
        else
            return false;
    }

    private String parseFieldString(String fieldName, String[] line) {
        return line[fileMap.get(fieldName.toLowerCase().trim())].trim();
    }

    private long parseFieldLong(String fieldName, String[] line) {
        String feature = parseFieldString(fieldName, line);
        long field = Long.parseLong(feature);
        return field;
    }

    private double parseFieldDouble(String fieldName, String[] line) {
        String feature = parseFieldString(fieldName, line);
        double field = Double.parseDouble(feature);
        return field;
    }

    /**
     * Parse an annotation String in CSV format
     *
     * Problem: this is a very ad-hoc solution...
     *
     * @param linea CSV line to be parsed into an Annotation
     * @return
     */
    private Annotation parseAnnotation(String linea) {
        String[] campos = linea.split(",");
        String templateA = campos[0].trim();
        String templateB = campos[2].trim();

        String attributeA = campos[1].trim();
        String attributeB = campos[3].trim();

        String propA = campos[4].trim();
        String propB = campos[5].trim();

//        String sM1 = campos[19].trim();
//        String sM2 = campos[20].trim();
//        String sM3 = campos[21].trim();
//        String sM4a = campos[22].trim();
//        String sM4b = campos[23].trim();
//        String sM5a = campos[24].trim();
//        String sM5b = campos[25].trim();
//
        String anotacion = campos[8].trim();
        long m1 = parseFieldLong("M1", campos);
        long m2 = parseFieldLong("M2", campos);
        long m3 = parseFieldLong("M3", campos);
        long m4a = parseFieldLong("M4a", campos);
        long m5a = parseFieldLong("M5a", campos);
        long m4b = parseFieldLong("M4b", campos);
        long m5b = parseFieldLong("M5b", campos);
//        try {
//            m1 = Long.parseLong(sM1);
//            m2 = Long.parseLong(sM2);
//            m3 = Long.parseLong(sM3);
//            m4a = Long.parseLong(sM4a);
//            m4b = Long.parseLong(sM4b);
//            m5a = Long.parseLong(sM5a);
//            m5b = Long.parseLong(sM5b);
//        } catch (NumberFormatException nfe) {
//            System.out.println("Error parsing "+sM1);
//        }
        Annotation entry = new Annotation(templateA, templateB, attributeA, attributeB,
                propA, propB, m1);
        entry.setAnotacion(anotacion);
        entry.setM2(m2);
        entry.setM3(m3);
        entry.setM4a(m4a);
        entry.setM4b(m4b);
        entry.setM5a(m5a);
        entry.setM5b(m5b);

        entry.setTb1(parseFieldBool("TB1", campos));
        entry.setTb2(parseFieldBool("TB2", campos));
        entry.setTb3(parseFieldBool("TB3", campos));
        entry.setTb4(parseFieldBool("TB4", campos));
        entry.setTb5(parseFieldBool("TB5", campos));
        entry.setTb6(parseFieldBool("TB6", campos));
        entry.setTb7(parseFieldBool("TB7", campos));
        entry.setTb8(parseFieldBool("TB8", campos));
        entry.setTb9(parseFieldBool("TB9", campos));
        entry.setTb10(parseFieldBool("TB10", campos));
        entry.setTb11(parseFieldBool("TB11", campos));

        entry.setC1(parseFieldDouble("C1", campos));
        entry.setC2(parseFieldDouble("C2", campos));
        entry.setC3a(parseFieldDouble("C3a", campos));
        entry.setC3b(parseFieldDouble("C3b", campos));

        entry.setClassA(parseFieldString("Class A", campos));
        entry.setClassB(parseFieldString("Class B", campos));
        entry.setDomainPropA(parseFieldString("Domain Property A", campos));
        entry.setDomainPropB(parseFieldString("Domain Property B", campos));
        entry.setRangePropA(parseFieldString("Range Property A", campos));
        entry.setRangePropB(parseFieldString("Range Property B", campos));

        entry.setLangA(langA);
        entry.setLangB(langB);

        return entry;
    }

    /**
     * Gets the maximum annotations number on the file
     * @return
     */
    public int getMaxNumber() {
        return lines.size();
    }
}
