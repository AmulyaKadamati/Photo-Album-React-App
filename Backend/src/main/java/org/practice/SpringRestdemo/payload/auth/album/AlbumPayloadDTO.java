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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlbumPayloadDTO {

    @NotBlank
    @Schema(description = "Album name", example = "Travel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "Description", example = "description", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

}
