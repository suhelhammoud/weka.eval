package weka.eval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper Class for All Jxxxx objects
 */
public class JObject {
    static Logger log = Logger.getLogger(JObject.class.getName());

    /**
     * @param fileName       path and name of file to save the object in
     * @param obj            instance to be mapped to json object and saved
     * @param usePrettyPrint use json pretty printing format
     */
    public static void save(String fileName, Object obj, boolean usePrettyPrint) {
        try {
            ObjectWriter ow = usePrettyPrint ?
                    new ObjectMapper().writerWithDefaultPrettyPrinter() :
                    new ObjectMapper().writer();
            ow.writeValue(new FileWriter(fileName), obj);
        } catch (IOException e) {
            log.log(Level.SEVERE, "save object", e);
        }
    }

    public static <E> E read(String fileName, Class<E> cls) {
        return read(new File(fileName), cls);
    }

    /**
     * Read josn file and map it into new java object intance of class : cls
     *
     * @param file : File object to be read from
     * @param cls  :
     * @return new java instance of type : cls
     */
    public static <E> E read(File file, Class<E> cls) {
        E result = null;
        try {
            if (!file.exists()) {
                System.err.println("error file " + file.getAbsolutePath() + " not found");
                throw new IOException();
            }
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(file, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Helper method to print all java bean properties, used only for logging.
     * DO NOT use in serialization, use getJsonString instead.
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        // TODO: use Jackson ObjectReader rather than java reflection
        StringJoiner result = new StringJoiner(", ", "{", "}");
        Class cls = obj.getClass();
        try {
            for (Field fld : cls.getDeclaredFields()) {
                final String name = fld.getName();
                final Object value = fld.get(obj);
                String valueString = value.getClass().isArray() ?
                        String.valueOf(Array.getLength(value)) :
                        value.toString();
                result.add(name + ": " + valueString);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getJsonString(Object obj) {
        try {
            return new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE, "Could not get json string", e);
            e.printStackTrace();
        }
        return "ERROR";
    }
}