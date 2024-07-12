package com.baseball.fractionassessment.model.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private OpenAiUsage usage;
    private List<OpenAiChoice> choices;
}
