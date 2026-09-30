package org.eclipse.birt.report.engine.emitter.csv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;

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

        return format((Date) value, pattern, Locale.ROOT);
    }

    static String format(Date value, String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(value);
    }
}
