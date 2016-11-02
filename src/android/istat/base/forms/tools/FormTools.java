package android.istat.base.forms.tools;

import android.annotation.SuppressLint;
import android.istat.base.forms.Form;
import android.istat.base.forms.utils.ClassFormLoader;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * @author istat
 */
public class FormTools {
    @SuppressLint("SimpleDateFormat")
    public final static Calendar parseToCalandar(Object obj) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = new GregorianCalendar();
        try {
            cal.setTime(df.parse(parseString(obj)));
        } catch (Exception e) {
            e.printStackTrace();
            // System.err.println("Fuck you:" + e);
        }
        return cal;
    }

    public final static String toSentense(String word, String endingPontuation) {
        word = beginByUpperCase(word);
        int index = word.indexOf(endingPontuation);
        // if(index!=word.length()-1)return word+endingPontuation;
        if (index <= 0) {
            return word + endingPontuation;
        }

        return word;
    }

    public final static String beginByUpperCase(String word) {
        if (word.length() > 1) {
            String begin = word.substring(0, 1).toUpperCase(
                    Locale.getDefault());
            word = begin + word.substring(1);
        }
        return word;
    }

    public final static int parseInt(Object o) {
        String s = parseString(o);
        return (int) parseDouble(s);
    }

    public final static double parseDouble(Object o) {
        String s = parseString(o);
        if (s == null || s.equals("")) {
            return 0;
        }
        if (s.trim().length() == 0) {
            return 0;
        }
        try {
            return Double.parseDouble(s.trim());

        } catch (Exception e) {
            return 0;
        }
    }

    public final static long parseLong(Object s) {
        return parseLong(parseString(s));
    }

    public final static Boolean parseBoolean(Object s) {
        return Boolean.valueOf(parseString(s));
    }

    public final static boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    public final static boolean isEmpty(Object string) {
        return string == null || "".equals(string + "");
    }

    public final static Boolean parseBoolean(Object s, boolean defaults) {
        if (isEmpty(s)) {
            return defaults;
        }
        return Boolean.valueOf(parseString(s));
    }

    public final static long parseLong(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        if (s.trim().length() == 0) {
            return 0;
        }
        try {
            double var = Double.parseDouble(s.trim());
            return (long) var;
        } catch (Exception e) {
            return 0;
        }
    }

    public final static int parseInt(int s) {
        return s;
    }

    public final static String ShortWord(String word, int max) {
        if (word.length() <= max) {
            return word;
        }
        return word.substring(0, max) + "...";
    }

    public final static float getpercentNumericValue(int state,
                                                     int STEP, boolean comma) {
        if (comma) {
            return ((float) (state) * 100 / (float) (STEP));
        } else {
            return (int) ((float) (state) * 100 / (float) (STEP));
        }
    }

    public final static String getpercentValue(int progresstate, int STEP,
                                               boolean comma) {
        if (comma) {
            return ((float) (progresstate) * 100 / (float) (STEP)) + "%";
        } else {
            return (int) ((float) (progresstate) * 100 / (float) (STEP)) + "%";
        }
    }

    public final static String getpercentValue(int progresstate, int STEP) {
        return ((float) (progresstate) * 100 / (float) (STEP)) + "%";
    }

    public final static float getpercentNumericValue(int progresstate, int STEP) {
        return ((float) (progresstate) * 100 / (float) (STEP));
    }

    /**
     * SweetNumber is used in order to get sweet Number definit like: if a<9
     * return "0a" else return just the number a
     *
     * @param a
     * @return a String that represent a "sweet" numeric fieldValue of that number.
     */
    public final static String sweetNumber(int a) {
        if (a > 9) {
            return "" + a;
        } else {
            return "0" + a;
        }
    }

    public final static String adjustNumber(float a) {
        if ((int) a == a) {
            return "" + (int) a;
        } else {
            return "" + a;
        }
    }

    public final static String parseString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    public final static Form concatenate(List<Form> forms) {
        Form form = new Form();
        return concatenate(form, forms);
    }

    public final static Form concatenate(Form form, List<Form> forms) {
        for (Form f : forms) {
            concatenate(form, f);
        }
        return form;
    }

    public final static Form concatenate(Form form, Form otherForm) {
        if (otherForm != null && form != null) {
            form.putAll(otherForm);
        }
        // Iterator<String> iterator = otherForm.keySet().iterator();
        // while (iterator.hasNext()) {
        // String fieldName = iterator.next();
        // if(form.containsKey(fieldName)){
        // fieldName=form.
        // }
        // }
        return form;
    }

    public static final Class<?> getGenericTypeClass(Class<?> baseClass, int genericIndex) {
        try {
            String className = ((ParameterizedType) baseClass
                    .getGenericSuperclass()).getActualTypeArguments()[genericIndex]
                    .toString().replaceFirst("class", "").trim();
            Class<?> clazz = Class.forName(className);
            return clazz;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }

    public static List<Field> getAllFieldIncludingPrivateAndSuper(Class<?> cLass) {
        List<Field> fields = new ArrayList<Field>();
        while (!cLass.equals(Object.class)) {
            Collections.addAll(fields, cLass.getDeclaredFields());
            cLass = cLass.getSuperclass();
        }
        return fields;
    }


}