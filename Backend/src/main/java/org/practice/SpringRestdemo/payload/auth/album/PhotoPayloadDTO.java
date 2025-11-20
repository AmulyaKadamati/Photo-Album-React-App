package org.practice.SpringRestdemo.payload.auth.album;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhotoPayloadDTO {

    @NotBlank
    @Schema(description = "Photo name", example = "selfie", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "Description of the photo", example = "Description", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

}
