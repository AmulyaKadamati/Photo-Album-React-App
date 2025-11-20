package org.practice.SpringRestdemo.payload.auth.album;

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
public class PhotoViewDTO {

    private long id;

    private String name;

    private String description;

}
