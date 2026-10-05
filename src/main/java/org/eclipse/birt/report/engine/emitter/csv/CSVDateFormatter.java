package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

final class CSVDateFormatter {

    private CSVDateFormatter() {
    }

    static String format(Object value, IStyle style) {
        if (!(value instanceof Date) || style == null) {
            return value == null ? "" : value.toString();
        }

        DataFormatValue dataFormat = style.getDataFormat();
        if (dataFormat == null) {
            return value.toString();
        }

        String pattern = dataFormat.getDateTimePattern();
        String locale = dataFormat.getDateTimeLocale();
        if (pattern == null) {
            pattern = dataFormat.getTimePattern();
            locale = dataFormat.getTimeLocale();
        }
        if (pattern == null) {
            pattern = dataFormat.getDatePattern();
            locale = dataFormat.getDateLocale();
        }
        return format((Date) value, pattern, parseLocale(locale));
    }

    static String format(Date value, String pattern, String locale) {
        return format(value, pattern, parseLocale(locale));
    }

    static String format(Date value, String pattern, Locale locale) {
        Locale effectiveLocale = locale == null ? Locale.getDefault() : locale;
        pattern = resolveNamedPattern(pattern, effectiveLocale);
        if (pattern == null || pattern.isEmpty()) {
            return value.toString();
        }

        System.out.println("[CSVDateFormatter] Pattern: " + pattern);
        System.out.println("[CSVDateFormatter] Locale: " + effectiveLocale);
        return new SimpleDateFormat(pattern, effectiveLocale).format(value);
    }

    private static String resolveNamedPattern(String pattern, Locale locale) {
        if ("General Date".equalsIgnoreCase(pattern)) {
            return getJavaDateTimePattern(DateFormat.LONG, locale);
        }
        if ("Long Date".equalsIgnoreCase(pattern)) {
            return getJavaDatePattern(DateFormat.LONG, locale);
        }
        if ("Medium Date".equalsIgnoreCase(pattern)) {
            return getJavaDatePattern(DateFormat.MEDIUM, locale);
        }
        if ("Short Date".equalsIgnoreCase(pattern)) {
            return getJavaDatePattern(DateFormat.SHORT, locale);
        }
        if ("Long Time".equalsIgnoreCase(pattern)) {
            return getJavaTimePattern(DateFormat.LONG, locale);
        }
        if ("Medium Time".equalsIgnoreCase(pattern)) {
            return getJavaTimePattern(DateFormat.MEDIUM, locale);
        }
        if ("Short Time".equalsIgnoreCase(pattern)) {
            return "kk:mm";
        }
        return pattern;
    }

    private static String getJavaDatePattern(int style, Locale locale) {
        DateFormat format = DateFormat.getDateInstance(style, locale);
        if (format instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) format).toPattern();
        }
        switch (style) {
            case DateFormat.SHORT:
                return "d/MM/yy";
            case DateFormat.MEDIUM:
                return "MMM d, yyyy";
            case DateFormat.LONG:
                return "MMMM d, yyyy";
            case DateFormat.FULL:
                return "EEEE, MMMM d, yyyy";
            default:
                return "MMM d, yyyy";
        }
    }

    private static String getJavaTimePattern(int style, Locale locale) {
        DateFormat format = DateFormat.getTimeInstance(style, locale);
        if (format instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) format).toPattern();
        }
        switch (style) {
            case DateFormat.SHORT:
                return "h:mm a";
            case DateFormat.MEDIUM:
            case DateFormat.LONG:
            case DateFormat.FULL:
            default:
                return "h:mm:ss a";
        }
    }

    private static String getJavaDateTimePattern(int style, Locale locale) {
        DateFormat format = DateFormat.getDateTimeInstance(style, style, locale);
        if (format instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) format).toPattern();
        }
        switch (style) {
            case DateFormat.SHORT:
                return "M/d/yy h:mm a";
            case DateFormat.MEDIUM:
                return "MMM d, yyyy h:mm:ss a";
            case DateFormat.LONG:
                return "MMMM d, yyyy h:mm:ss a";
            case DateFormat.FULL:
                return "EEEE, MMMM d, yyyy h:mm:ss a";
            default:
                return "MMM d, yyyy h:mm:ss a";
        }
    }

    private static Locale parseLocale(String locale) {
        if (locale == null || locale.trim().isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(locale.replace('_', '-'));
    }
}
