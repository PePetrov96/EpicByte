package com.project.EpicByte.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldNamesGeneratorTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private FieldNamesGenerator fieldNamesGenerator;

    @Test
    void getFieldNames_success_simpleFields() {
        when(messageSource.getMessage(any(), any(), any())).thenReturn("testMessage");

        List<FieldNamesGenerator.FieldEntity> fields = fieldNamesGenerator.getFieldNames("book", false);

        assertFalse(fields.isEmpty(), "Fields should be populated.");

        fields.forEach(fieldEntity -> {
            assertNotNull(fieldEntity.getFieldName(), "Field name should not be null.");
            assertNotNull(fieldEntity.getFieldType(), "Field type should not be null.");
            assertTrue(fieldEntity.getPrettyFieldName().contains("testMessage"),
                    "Pretty field name should be populated with the mocked message");
        });
    }

    @Test
    void getFieldNames_success_complexFields() {
        when(messageSource.getMessage(any(), any(), any())).thenReturn("testMessage");

        List<FieldNamesGenerator.FieldEntity> fields = fieldNamesGenerator.getFieldNames("book", true);

        assertFalse(fields.isEmpty(), "Fields should be populated.");

        fields.forEach(fieldEntity -> {
            assertNotNull(fieldEntity.getFieldName(), "Field name should not be null.");
            assertNotNull(fieldEntity.getFieldType(), "Field type should not be null.");
            assertTrue(fieldEntity.getPrettyFieldName().contains("testMessage"),
                    "Pretty field name should be populated with the mocked message");
        });
    }

    @Test
    void getPrettyName() {
        String name = "testName";
        when(messageSource.getMessage(any(), any(), any())).thenReturn("testMessage");

        String prettyName = fieldNamesGenerator.getPrettyName(name, Locale.getDefault());

        assertEquals("testMessage", prettyName, "prettyName should be populated with the mocked message");
        verify(messageSource).getMessage(any(), any(), any(Locale.class));
    }
}