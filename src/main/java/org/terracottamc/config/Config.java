package org.terracottamc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (c) 2021, TerracottaMC
 * All rights reserved.
 *
 * <p>
 * This project is licensed under the BSD 3-Clause License which
 * can be found in the root directory of this source tree
 *
 * @author Kaooot
 * @version 1.0
 */
public class Config {

    private File file;
    private Map<String, Object> configurationData = new LinkedHashMap<>();

    private final ConfigType configType;
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
                if (src == src.intValue()) {
                    return new JsonPrimitive(src.intValue());
                } else if (src == src.longValue()) {
                    return new JsonPrimitive(src.longValue());
                }

                return new JsonPrimitive(src);
            }).create();
    private final Properties properties = new Properties();
    private final Yaml yaml = new Yaml();

    /**
     * Creates a new {@link org.terracottamc.config.Config} with given {@link org.terracottamc.config.ConfigType}
     *
     * @param configType which is used to define the type of this {@link org.terracottamc.config.Config}
     */
    public Config(final ConfigType configType) {
        this.configType = configType;
    }

    /**
     * Loads this configuration and its data from the given {@link java.io.File}
     *
     * @param file the holder of the configuration data
     *
     * @return a fresh {@link org.terracottamc.config.Config}
     */
    public Config load(final File file) {
        if (file == null) {
            throw new NullPointerException("The file cannot be null");
        }

        if (!file.exists()) {
            try {
                throw new FileNotFoundException("The file " + file.getName() + " could not be found");
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        this.file = file;

        switch (this.configType) {
            case JSON:
                try {
                    final Map<String, Object> map = this.gson.fromJson(new FileReader(this.file), LinkedHashMap.class);

                    this.configurationData = map == null ? new LinkedHashMap<>() : map;
                } catch (final FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case YAML:
                try {
                    final Map<String, Object> map = this.yaml.load(new FileReader(this.file));

                    this.configurationData = map == null ? new LinkedHashMap<>() : map;
                } catch (final FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case PROPERTIES:
                try {
                    this.properties.load(new BufferedReader(new InputStreamReader(new FileInputStream(this.file))));
                } catch (final IOException e) {
                    e.printStackTrace();
                }

                break;
        }

        return this;
    }

    /**
     * Sets a value with the given key to this {@link org.terracottamc.config.Config}
     * Note that property files are only accepting {@link java.lang.String} values
     *
     * @param key   which is the holder of the value that should be set
     * @param value the value that should be set
     */
    public void setValue(final String key, final Object value) {
        switch (this.configType) {
            case JSON:
            case YAML:
                this.configurationData.put(key, value);
                break;
            case PROPERTIES:
                this.properties.setProperty(key, String.valueOf(value));
                break;
        }
    }

    /**
     * Adds a default value with the given key to this {@link org.terracottamc.config.Config}
     *
     * @param key   which is the holder of the value that should be added as default value
     * @param value the value that should be added as default value
     */
    public void addDefault(final String key, final Object value) {
        if (!this.exists(key)) {
            this.setValue(key, value);
        }
    }

    /**
     * Adds a bunch of default values to this {@link org.terracottamc.config.Config}
     *
     * @param defaultValues the values which should be added to this {@link org.terracottamc.config.Config}
     */
    public void addDefaults(final LinkedHashMap<String, Object> defaultValues) {
        boolean containsKey = false;

        for (final String key : defaultValues.keySet()) {
            if (this.exists(key)) {
                containsKey = true;
                break;
            }
        }

        if (!containsKey) {
            this.configurationData.putAll(defaultValues);
        }
    }

    /**
     * Saves this {@link org.terracottamc.config.Config}
     */
    public void save() {
        try (final FileWriter fileWriter = new FileWriter(this.file)) {
            switch (this.configType) {
                case JSON:
                    fileWriter.write(this.gson.toJson(this.configurationData));
                    break;
                case YAML:
                    fileWriter.write(this.yaml.dump(this.configurationData));
                    break;
                case PROPERTIES:
                    this.properties.store(fileWriter, "");
                    break;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Proofs whether the given key already exists in this {@link org.terracottamc.config.Config}
     *
     * @param key that is used for the check and probably holds a value
     *
     * @return true, when the searched key exists, otherwise false
     */
    public boolean exists(final String key) {
        switch (this.configType) {
            case JSON:
            case YAML:
                return this.configurationData.containsKey(key);
            case PROPERTIES:
                return this.properties.getProperty(key) != null;
        }

        return false;
    }

    /**
     * Retrieves a fresh {@link java.lang.Object} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Object}
     */
    public Object getObject(final String key) {
        switch (this.configType) {
            case JSON:
            case YAML:
                return this.configurationData.get(key);
            case PROPERTIES:
                return this.properties.getProperty(key);
        }

        return null;
    }

    /**
     * Retrieves a fresh {@link java.lang.Boolean} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Boolean}
     */
    public boolean getBoolean(final String key) {
        return (boolean) this.getObject(key);
    }

    /**
     * Retrieves a fresh {@link java.lang.Byte} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Byte}
     */
    public byte getByte(final String key) {
        return ((Number) this.getObject(key)).byteValue();
    }

    /**
     * Retrieves a fresh {@link java.lang.Short} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Short}
     */
    public short getShort(final String key) {
        return ((Number) this.getObject(key)).shortValue();
    }

    /**
     * Retrieves a fresh {@link java.lang.Character} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Character}
     */
    public char getChar(final String key) {
        return (char) this.getObject(key);
    }

    /**
     * Retrieves a fresh {@link java.lang.Integer} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Integer}
     */
    public int getInt(final String key) {
        return ((Number) this.getObject(key)).intValue();
    }

    /**
     * Retrieves a fresh {@link java.lang.Float} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Float}
     */
    public float getFloat(final String key) {
        return ((Number) this.getObject(key)).floatValue();
    }

    /**
     * Retrieves a fresh {@link java.lang.Double} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Object}
     */
    public double getDouble(final String key) {
        return ((Number) this.getObject(key)).doubleValue();
    }

    /**
     * Retrieves a fresh {@link java.lang.Long} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Object}
     */
    public long getLong(final String key) {
        return ((Number) this.getObject(key)).longValue();
    }

    /**
     * Retrieves a fresh {@link java.lang.String} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.lang.Object}
     */
    public String getString(final String key) {
        return (String) this.getObject(key);
    }

    /**
     * Retrieves a fresh {@link java.util.List} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.util.List}
     */
    public List<?> getList(final String key) {
        return (List<?>) this.getObject(key);
    }

    /**
     * Retrieves a fresh {@link java.util.Map} value from this {@link org.terracottamc.config.Config} with given key
     *
     * @param key which is the holder of the value that should be retrieved
     *
     * @return a fresh {@link java.util.Map}
     */
    public Map<?, ?> getMap(final String key) {
        return (Map<?, ?>) this.getObject(key);
    }
}