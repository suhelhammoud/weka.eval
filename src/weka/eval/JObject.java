package weka.eval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper java8 interface for All Jxxxx objects, all JXXX object should implement this interface
 */
public interface JObject {
    static Logger log = Logger.getLogger(JObject.class.getName());

    /**
     * @param fileName       path and name ofInstances file to save the object in
     * @param obj            instance to be mapped to json object and saved
     * @param usePrettyPrint use json pretty printing format
     */
    static void save(String fileName, Object obj, boolean usePrettyPrint) {
        try {
            ObjectWriter ow = usePrettyPrint ?
                    new ObjectMapper().writerWithDefaultPrettyPrinter() :
                    new ObjectMapper().writer();
            ow.writeValue(new FileWriter(fileName), obj);
        } catch (IOException e) {
            log.log(Level.SEVERE, "save object", e);
        }
    }

    default void save(String fileName, boolean usePrettyPrint) {
        JObject.save(fileName, this, usePrettyPrint);
    }

    default <E> E read(String fileName) {
        return JObject.read(fileName, (Class<E>) this.getClass());
    }

    static <E> E read(String fileName, Class<E> cls) {
        return read(new File(fileName), cls);
    }

    /**
     * Read josn file and map it into new java object intance ofInstances class : cls
     *
     * @param file : File object to be read from
     * @param cls  :
     * @return new java instance ofInstances type : cls
     */
    static <E> E read(File file, Class<E> cls) {
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
    static String getString(Object obj) {
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

    default String getString() {
        return JObject.getString(this);
    }

    static String getJsonString(Object obj) {
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

    default String getJsonString() {
        return JObject.getJsonString(this);
    }


    /**
     * Used by all JXXX classes
     *
     * @param cls
     * @param withHeaders include the columns header in schema
     * @return
     */
    static CsvSchema schema(Class cls, boolean withHeaders) {
        CsvMapper mapper = new CsvMapper();
        return withHeaders ?
                mapper.schemaFor(cls).withHeader() :
                mapper.schemaFor(cls);
    }

    default CsvSchema schema(boolean withHeaders) {
        return JObject.schema(this.getClass(), withHeaders);
    }

    static <T extends JObject> String getCsv(List<T> list, boolean withHeaders)
            throws JsonProcessingException {
        //TODO allow empty list, get T class using Class<T> parameter
        assert list.size() > 0;
        CsvSchema schema = list.get(0).schema(withHeaders);
//        CsvSchema schema = JObject.schema(list.get(0).getClass(), withHeaders);
        return new CsvMapper().writer(schema).writeValueAsString(list);
    }

    /**
     * Used for debugging only, not efficient.
     *
     * @param withHeader
     * @return
     */
    default String getCsvString(boolean withHeader) {
        try {
            return new CsvMapper()
                    .writer(this.schema(withHeader))
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Error " + this.getString();
    }

}
