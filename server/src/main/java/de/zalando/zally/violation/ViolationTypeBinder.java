package de.zalando.zally.violation;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class ViolationTypeBinder extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        final ViolationType value = StringUtils.hasText(text) ? ViolationType.valueOf(text.toUpperCase()) : null;
        setValue(value);
    }
}
