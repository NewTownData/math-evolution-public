/*
 * Copyright 2023 Voyta Krizek, https://github.com/NewTownData
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.newtowndata.math.config;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.logging.Logger;

public final class ConfigurationLoader {

  private static final Logger LOG = Logger.getLogger(ConfigurationLoader.class.getName());

  private ConfigurationLoader() {
  }

  public static Configuration fromStream(InputStream inputStream) {
    try {
      Configuration config = new Configuration();

      Properties props = new Properties();
      props.load(inputStream);

      Field[] fields = Configuration.class.getDeclaredFields();
      for (Field field : fields) {
        String name = field.getName();
        String propertyName = fieldToPropertyName(name);

        String stringValue = props.getProperty(propertyName);
        if (stringValue == null || stringValue.isEmpty()) {
          LOG.info("No value for field " + propertyName);
          continue;
        }

        field.setAccessible(true);

        Class<?> fieldType = field.getType();
        if (int.class.equals(fieldType)) {
          field.setInt(config, loadInt(props, propertyName));
        } else if (boolean.class.equals(fieldType)) {
          field.setBoolean(config, loadBool(props, propertyName));
        } else if (String.class.equals(fieldType)) {
          field.set(config, stringValue);
        } else if (double.class.equals(fieldType)) {
          field.setDouble(config, loadDouble(props, propertyName));
        } else {
          throw new IllegalStateException(
              "Unsupported field " + name + " type " + fieldType.getName());
        }
      }
      return config;
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot load configuration from stream", e);
    }
  }

  static String fieldToPropertyName(String name) {
    StringBuilder sb = new StringBuilder();

    for (char c : name.toCharArray()) {
      if (Character.isUpperCase(c)) {
        sb.append('_').append(Character.toLowerCase(c));
      } else {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  private static int loadInt(Properties props, String key) {
    try {
      return Integer.parseInt(props.getProperty(key));
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot load key " + key, e);
    }
  }

  private static double loadDouble(Properties props, String key) {
    try {
      return Double.parseDouble(props.getProperty(key));
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot load key " + key, e);
    }
  }

  private static boolean loadBool(Properties props, String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }


}
