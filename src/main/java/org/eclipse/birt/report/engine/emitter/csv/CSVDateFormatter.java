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
        if (pattern == null || pattern.isEmpty()) {
            return value.toString();
        }

        System.out.println("[CSVDateFormatter] Pattern: " + pattern);
        System.out.println("[CSVDateFormatter] Locale: " + effectiveLocale);
        return new SimpleDateFormat(pattern, effectiveLocale).format(value);
    }

    private static Locale parseLocale(String locale) {
        if (locale == null || locale.trim().isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(locale.replace('_', '-'));
    }
}
