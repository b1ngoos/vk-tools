package com.b1ngoos.walllikes.tool;

import com.b1ngoos.walllikes.App;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenTool {

    private static final String propFileName = "config.properties";

    private static String token = null;

    public static String getToken() {

        if(token == null) {
            token = getPropTokenValues();
        }

        return token;
    }

    private static String getPropTokenValues()  {

        String token = "";

        try(InputStream inputStream = App.class.getClassLoader().getResourceAsStream(propFileName)){
            Properties prop = new Properties();
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            token = prop.getProperty("token");
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
        return token;
    }

}
