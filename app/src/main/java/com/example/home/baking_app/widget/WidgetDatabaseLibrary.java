package com.example.home.baking_app.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


class WidgetDatabaseLibrary {

    private SharedPreferences preferences;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";

    public WidgetDatabaseLibrary(Context appContext) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public Bitmap getImage(String path) {
        Bitmap bitmapFromPath = null;

        try {
            bitmapFromPath = BitmapFactory.decodeFile(path);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return bitmapFromPath;
    }

    public String getSavedImagePath() {
        return this.lastImagePath;
    }

    public String putImage(String theFolder, String theImageName, Bitmap theBitmap) {
        if(theFolder != null && theImageName != null && theBitmap != null) {
            this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
            String mFullPath = this.setupFullPath(theImageName);
            if(!mFullPath.equals("")) {
                this.lastImagePath = mFullPath;
                this.saveBitmap(mFullPath, theBitmap);
            }

            return mFullPath;
        } else {
            return null;
        }
    }

    public boolean putImageWithFullPath(String fullPath, Bitmap theBitmap) {
        return fullPath != null && theBitmap != null && this.saveBitmap(fullPath, theBitmap);
    }

    private String setupFullPath(String imageName) {
        File mFolder = new File(Environment.getExternalStorageDirectory(), this.DEFAULT_APP_IMAGEDATA_DIRECTORY);
        if(isExternalStorageReadable() && isExternalStorageWritable() && !mFolder.exists() && !mFolder.mkdirs()) {
            Log.e("ERROR", "Failed to setup folder");
            return "";
        } else {
            return mFolder.getPath() + '/' + imageName;
        }
    }

    private boolean saveBitmap(String fullPath, Bitmap bitmap) {
        if(fullPath != null && bitmap != null) {
            boolean fileCreated = false;
            boolean bitmapCompressed = false;
            boolean streamClosed = false;
            File imageFile = new File(fullPath);
            if(imageFile.exists() && !imageFile.delete()) {
                return false;
            } else {
                try {
                    fileCreated = imageFile.createNewFile();
                } catch (IOException var19) {
                    var19.printStackTrace();
                }

                FileOutputStream out = null;

                try {
                    out = new FileOutputStream(imageFile);
                    bitmapCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception var18) {
                    var18.printStackTrace();
                    bitmapCompressed = false;
                } finally {
                    if(out != null) {
                        try {
                            out.flush();
                            out.close();
                            streamClosed = true;
                        } catch (IOException var17) {
                            var17.printStackTrace();
                            streamClosed = false;
                        }
                    }

                }

                return fileCreated && bitmapCompressed && streamClosed;
            }
        } else {
            return false;
        }
    }

    public int getInt(String key) {
        return this.preferences.getInt(key, 0);
    }

    public ArrayList<Integer> getListInt(String key) {
        String[] myList = TextUtils.split(this.preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList();
        Iterator var5 = arrayToList.iterator();

        while(var5.hasNext()) {
            String item = (String)var5.next();
            newList.add(Integer.valueOf(Integer.parseInt(item)));
        }

        return newList;
    }

    public long getLong(String key, long defaultValue) {
        return this.preferences.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return this.preferences.getFloat(key, 0.0F);
    }

    public double getDouble(String key, double defaultValue) {
        String number = this.getString(key);

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException var6) {
            return defaultValue;
        }
    }

    public ArrayList<Double> getListDouble(String key) {
        String[] myList = TextUtils.split(this.preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList(Arrays.asList(myList));
        ArrayList<Double> newList = new ArrayList();
        Iterator var5 = arrayToList.iterator();

        while(var5.hasNext()) {
            String item = (String)var5.next();
            newList.add(Double.valueOf(Double.parseDouble(item)));
        }

        return newList;
    }

    public String getString(String key) {
        return this.preferences.getString(key, "");
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList(Arrays.asList(TextUtils.split(this.preferences.getString(key, ""), "‚‗‚")));
    }

    public boolean getBoolean(String key) {
        return this.preferences.getBoolean(key, false);
    }

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> myList = this.getListString(key);
        ArrayList<Boolean> newList = new ArrayList();
        Iterator var4 = myList.iterator();

        while(var4.hasNext()) {
            String item = (String)var4.next();
            if(item.equals("true")) {
                newList.add(Boolean.valueOf(true));
            } else {
                newList.add(Boolean.valueOf(false));
            }
        }

        return newList;
    }

    public ArrayList<Object> getListObject(String key, Class<?> mClass) {
        return this.getListObject(key, mClass, new Gson());
    }

    public Object getObject(String key, Class<?> classOfT) {
        return this.getObject(key, classOfT, new Gson());
    }

    public ArrayList<Object> getListObject(String key, Class<?> mClass, Gson gson) {
        ArrayList<String> objStrings = this.getListString(key);
        ArrayList<Object> objects = new ArrayList();
        Iterator var6 = objStrings.iterator();

        while(var6.hasNext()) {
            String jObjString = (String)var6.next();
            Object value = gson.fromJson(jObjString, mClass);
            objects.add(value);
        }

        return objects;
    }

    public Object getObject(String key, Class<?> classOfT, Gson gson) {
        String json = this.getString(key);
        Object value = gson.fromJson(json, classOfT);
        if(value == null) {
            throw new NullPointerException();
        } else {
            return value;
        }
    }

    public void putInt(String key, int value) {
        this.checkForNullKey(key);
        this.preferences.edit().putInt(key, value).apply();
    }

    public void putListInt(String key, ArrayList<Integer> intList) {
        this.checkForNullKey(key);
        Integer[] myIntList = (Integer[])intList.toArray(new Integer[intList.size()]);
        this.preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    public void putLong(String key, long value) {
        this.checkForNullKey(key);
        this.preferences.edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        this.checkForNullKey(key);
        this.preferences.edit().putFloat(key, value).apply();
    }

    public void putDouble(String key, double value) {
        this.checkForNullKey(key);
        this.putString(key, String.valueOf(value));
    }

    public void putListDouble(String key, ArrayList<Double> doubleList) {
        this.checkForNullKey(key);
        Double[] myDoubleList = (Double[])doubleList.toArray(new Double[doubleList.size()]);
        this.preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply();
    }

    public void putString(String key, String value) {
        this.checkForNullKey(key);
        this.checkForNullValue(value);
        this.preferences.edit().putString(key, value).apply();
    }

    public void putListString(String key, ArrayList<String> stringList) {
        this.checkForNullKey(key);
        String[] myStringList = (String[])stringList.toArray(new String[stringList.size()]);
        this.preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void putBoolean(String key, boolean value) {
        this.checkForNullKey(key);
        this.preferences.edit().putBoolean(key, value).apply();
    }

    public void putListBoolean(String key, ArrayList<Boolean> boolList) {
        this.checkForNullKey(key);
        ArrayList<String> newList = new ArrayList();
        Iterator var4 = boolList.iterator();

        while(var4.hasNext()) {
            Boolean item = (Boolean)var4.next();
            if(item.booleanValue()) {
                newList.add("true");
            } else {
                newList.add("false");
            }
        }

        this.putListString(key, newList);
    }

    public void putObject(String key, Object obj) {
        this.putObject(key, obj, new Gson());
    }

    public void putListObject(String key, ArrayList<Object> objArray) {
        this.putListObject(key, objArray, new Gson());
    }

    public void putObject(String key, Object obj, Gson gson) {
        this.checkForNullKey(key);
        this.putString(key, gson.toJson(obj));
    }

    public void putListObject(String key, ArrayList<Object> objArray, Gson gson) {
        this.checkForNullKey(key);
        ArrayList<String> objStrings = new ArrayList();
        Iterator var5 = objArray.iterator();

        while(var5.hasNext()) {
            Object obj = var5.next();
            objStrings.add(gson.toJson(obj));
        }

        this.putListString(key, objStrings);
    }

    public boolean contains(String key) {
        return this.preferences.contains(key);
    }

    public void remove(String key) {
        this.preferences.edit().remove(key).apply();
    }

    public boolean deleteImage(String path) {
        return (new File(path)).delete();
    }

    public void clear() {
        this.preferences.edit().clear().apply();
    }

    public Map<String, ?> getAll() {
        return this.preferences.getAll();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state) || "mounted_ro".equals(state);
    }

    public void checkForNullKey(String key) {
        if(key == null) {
            throw new NullPointerException();
        }
    }

    public void checkForNullValue(String value) {
        if(value == null) {
            throw new NullPointerException();
        }
    }
}
