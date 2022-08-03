package com.infomansion.server.global.util.restdocs;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class RestDocsUtil {

    public static List<FieldDescriptor> common(FieldDescriptor data) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        fieldDescriptors.add( fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 성공 여부"));
        fieldDescriptors.add(fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"));
        fieldDescriptors.add(data);
        return fieldDescriptors;
    }

}
