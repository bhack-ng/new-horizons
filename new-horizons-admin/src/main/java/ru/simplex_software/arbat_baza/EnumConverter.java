package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import ru.simplex_software.zkutils.PropConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnumConverter implements Converter<String, Enum, Component> {

    private static final Logger LOG = LoggerFactory.getLogger(EnumConverter.class);

    private static Map<Class<? extends Enum>, Properties> enumPropertiesHashMap = new HashMap<>();
    private String defaultPackage = null;

    public EnumConverter(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public EnumConverter() {
    }

    /**
     * Конвертация в элемент для отображения.
     */
    public String coerceToUi(Enum beanProp, Component component, BindContext context) {
        Properties prop = getPropertiesByCtx(context);
        if (beanProp == null) {
            return null;
        }
        String key = beanProp.name();
        return prop.getProperty(key);
    }

    /**
     * Конвертация в элемент для хранения.
     */
    public Enum coerceToBean(String compAttr, Component component, BindContext context) {
        Properties prop = getPropertiesByCtx(context);
        Class<? extends Enum> classByCtx = getClassByCtx(context);
        for (Map.Entry e : prop.entrySet()) {
            if (e.getValue().equals(compAttr)) {
                String key = (String) e.getKey();
                return Enum.valueOf(classByCtx, key);
            }
        }
        return null;
    }

    private Properties getPropertiesByCtx(BindContext context) {
        String enumClassStr = (String) context.getConverterArg("enumClass");
        enumClassStr = enumClassStr.replace(" ", "");
        if (defaultPackage != null) {
            enumClassStr = defaultPackage + "." + enumClassStr;
        }

        Properties prop;
        try {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(enumClassStr);
            prop = getProperties(enumClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

    private Class<? extends Enum> getClassByCtx(BindContext context) {
        String enumClassStr = (String) context.getConverterArg("enumClass");
        enumClassStr = enumClassStr.replace(" ", "");
        if (defaultPackage != null) {
            enumClassStr = defaultPackage + "." + enumClassStr;
        }

        try {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(enumClassStr);
            return enumClass;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение списка свойств перечисления.
     */
    private Properties getProperties(Class<? extends Enum> enumClass) throws IOException {
        Properties properties = enumPropertiesHashMap.get(enumClass);
        if (properties != null) {
            return properties;
        }

        // Загрузка свойств из файла.
        synchronized (enumPropertiesHashMap) {
            String fileName;
            fileName = "translations/" + enumClass.getCanonicalName() + ".properties";
            InputStream stream = PropConverter.class.getClassLoader().getResourceAsStream(fileName);

            // Файл не найден.
            if (stream == null) {
                throw new FileNotFoundException("Не удалось найти файл свойств для " + enumClass.getName());
            }

            properties = new Properties();
            properties.load(stream);
            enumPropertiesHashMap.put(enumClass, properties);
            return properties;
        }
    }
}
