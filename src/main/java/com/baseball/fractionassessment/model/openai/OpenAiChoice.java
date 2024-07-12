package com.baseball.fractionassessment.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiChoice {
    private OpenAiMsg message;
    private Object logprobs;
    @JsonProperty("finish_reason")
    private String finishReason;
    private int index;
}
