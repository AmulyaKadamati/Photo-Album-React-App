package org.practice.SpringRestdemo.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.practice.SpringRestdemo.Models.Account;
import org.practice.SpringRestdemo.Models.Album;
import org.practice.SpringRestdemo.Models.Photo;
import org.practice.SpringRestdemo.payload.auth.album.AlbumPayloadDTO;
import org.practice.SpringRestdemo.payload.auth.album.AlbumViewDTO;
import org.practice.SpringRestdemo.payload.auth.album.PhotoDTO;
import org.practice.SpringRestdemo.payload.auth.album.PhotoPayloadDTO;
import org.practice.SpringRestdemo.payload.auth.album.PhotoViewDTO;
import org.practice.SpringRestdemo.service.AccountService;
import org.practice.SpringRestdemo.service.AlbumService;
import org.practice.SpringRestdemo.service.PhotoService;
import org.practice.SpringRestdemo.util.AlbumError;
import org.practice.SpringRestdemo.util.AppUtils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Album Controller", description = "Controller for album management")
@Slf4j
public class AlbumController {

    static final String PHOTOS_FOLDER_NAME = "photos";
    static final String THUMBNAIL_FOLDER_NAME = "thumbnails";
    static final int THUMBNAIL_WIDTH = 300;
    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    // This method is used to add an album
    // It takes AlbumPayloadDTO as input and returns AlbumViewDTO
    // It also requires authentication to access this endpoint
    @PostMapping(value = "/albums/add", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Album added")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @Operation(summary = "Add an album")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            Authentication authentication) {
        try {
            Album album = new Album();
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            Account account = accountService.findByEmail(authentication.getName()).get();
            album.setAccount(account);
            album = albumService.save(album);
            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), null);
            return ResponseEntity.ok(albumViewDTO);
        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + " : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // This method is used to view all albums of the authenticated user
    // It returns a list of AlbumViewDTO
    // It requires authentication to access this endpoint
    @GetMapping(value = "/albums", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "List of albums")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @Operation(summary = "View albums")
    public List<AlbumViewDTO> albums(Authentication authentication) {
        Account account = accountService.findByEmail(authentication.getName()).get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        for (Album album : albumService.findByAccount_id(account.getId())) {

            List<PhotoDTO> photos = new ArrayList<>();
            for (Photo photo : photoService.findByAlbum_id(album.getId())) {
                String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(),
                        link));

            }
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos));

        }

        return albums;
    }

    // This method is used to view a specific album by its ID
    // It returns AlbumViewDTO if the album exists and belongs to the authenticated
    // user
    // It requires authentication to access this endpoint
    @GetMapping(value = "/albums/{album_id}", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Album details")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "List album by albumId")
    public ResponseEntity<AlbumViewDTO> albums_by_id(@PathVariable long album_id, Authentication authentication) {
        Account account = accountService.findByEmail(authentication.getName()).get();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (account.getId() != album.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<PhotoDTO> photos = new ArrayList<>();

        for (Photo photo : photoService.findByAlbum_id(album.getId())) {
            String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
            photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link));

        }
        AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos);

        return ResponseEntity.ok(albumViewDTO);
    }

    // This method is used to update an existing album
    // It takes AlbumPayloadDTO as input and returns the updated AlbumViewDTO
    // It requires authentication to access this endpoint
    @PutMapping(value = "/albums/{album_id}/update-album", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Album updated")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Update Album")
    public ResponseEntity<AlbumViewDTO> update_album(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            @PathVariable long album_id, Authentication authentication) {
        Account account = accountService.findByEmail(authentication.getName()).get();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        album.setName(albumPayloadDTO.getName());
        album.setDescription(albumPayloadDTO.getDescription());
        albumService.save(album);
        List<PhotoDTO> photos = new ArrayList<>();

        for (Photo photo : photoService.findByAlbum_id(album.getId())) {
            String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
            photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link));
        }
        AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos);
        return ResponseEntity.ok(albumViewDTO);
    }

    // This method is used to upload multiple photos to an album
    // It takes MultipartFile array as input and returns a list of HashMaps
    // containing success and error messages
    // It requires authentication to access this endpoint
    @PostMapping(value = "/albums/{album_id}/upload-photos", consumes = "multipart/form-data")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "201", description = "Photo uploaded")
    @ApiResponse(responseCode = "400", description = "Please check payload or token")
    @Operation(summary = "Upload photos")
    public ResponseEntity<List<HashMap<String, List<?>>>> photos(@RequestPart(required = true) MultipartFile[] files,
            @PathVariable long album_id, Authentication authentication) {
        Account account = accountService.findByEmail(authentication.getName()).get();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<PhotoViewDTO> fileNameWithSuccess = new ArrayList<>();
        List<String> fileNameWithError = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();
            if (contentType.equals("image/png")
                    || contentType.equals("image/jpg")
                    || contentType.equals("image/jpeg")) {

                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;

                try {
                    String fileName = file.getOriginalFilename();
                    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

                    String final_photo_name = generatedString + fileName;

                    String absolute_fileLocation = AppUtils.get_photo_upload_path(final_photo_name, PHOTOS_FOLDER_NAME,
                            album_id);
                    Path path = Paths.get(absolute_fileLocation);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    Photo photo = new Photo();
                    photo.setName(fileName);
                    photo.setDescription(null);
                    photo.setFileName(final_photo_name);
                    photo.setOriginalFileName(fileName);
                    photo.setAlbum(album);
                    photoService.save(photo);

                    PhotoViewDTO photoViewDTO = new PhotoViewDTO(photo.getId(), photo.getName(),
                            photo.getDescription());
                    fileNameWithSuccess.add(photoViewDTO);

                    BufferedImage thumbImg = AppUtils.getThumbnail(file, THUMBNAIL_WIDTH);
                    File thumbnail_Location = new File(
                            AppUtils.get_photo_upload_path(final_photo_name, THUMBNAIL_FOLDER_NAME, album_id));

                    ImageIO.write(thumbImg, file.getContentType().split("/")[1], thumbnail_Location);

                } catch (Exception e) {
                    log.debug(AlbumError.PHOTO_UPLOAD_ERROR + ": " + e.getMessage());
                    fileNameWithError.add(file.getOriginalFilename());
                }
            } else {
                fileNameWithError.add(file.getOriginalFilename());
            }

        });

        HashMap<String, List<?>> result = new HashMap<>();
        result.put("SUCCESS", fileNameWithSuccess);
        result.put("ERROR", fileNameWithError);

        List<HashMap<String, List<?>>> response = new ArrayList<>();
        response.add(result);
        return ResponseEntity.ok(response);
    }

    // This method is used to update an existing photo in an album
    // It takes PhotoPayloadDTO as input and returns PhotoViewDTO
    // It requires authentication to access this endpoint
    @PutMapping(value = "/albums/{album_id}/photos/{photo_id}/update-photo", consumes = "application/json", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "201", description = "Photo updated")
    @ApiResponse(responseCode = "400", description = "Please check payload or token")
    @Operation(summary = "Update photo")
    public ResponseEntity<PhotoViewDTO> update_photo(@RequestBody PhotoPayloadDTO photoPayloadDTO,
            @PathVariable long album_id,
            @PathVariable Long photo_id, Authentication authentication) {
        try {
            Account account = accountService.findByEmail(authentication.getName()).get();
            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();

                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Optional<Photo> optionalPhoto = photoService.findById(photo_id);
            if (optionalPhoto.isPresent()) {
                Photo photo = optionalPhoto.get();
                if (photo.getAlbum().getId() != album_id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
                photo.setName(photoPayloadDTO.getName());
                photo.setDescription(photoPayloadDTO.getDescription());
                photoService.save(photo);
                PhotoViewDTO photoViewDTO = new PhotoViewDTO(photo.getId(), photo.getName(), photo.getDescription());
                return ResponseEntity.ok(photoViewDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // This method is used to delete an album and all its photos
    // It requires authentication to access this endpoint
    // It returns a response indicating whether the album was deleted successfully or not
    @DeleteMapping(value = "/albums/{album_id}/delete-album", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "202", description = "Album deleted")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @Operation(summary = "Delete an Album")
    public ResponseEntity<String> delete_album(@PathVariable long album_id, Authentication authentication) {
        try {
            Account account = accountService.findByEmail(authentication.getName()).get();
            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            for (Photo photo : photoService.findByAlbum_id(album_id)) {
                AppUtils.delete_photo_from_path(photo.getFileName(), PHOTOS_FOLDER_NAME, album_id);
                AppUtils.delete_photo_from_path(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);

                photoService.delete(photo);
            }
            albumService.deleteAlbum(album);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // This method is used to delete a photo from an album
    // It requires authentication to access this endpoint
    // It returns a response indicating whether the photo was deleted successfully or not
    @DeleteMapping(value = "/albums/{album_id}/photos/{photo_id}/delete-photo")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "202", description = "Photo deleted")
    @ApiResponse(responseCode = "400", description = "Please check payload or token")
    @Operation(summary = "Delete photo")
    public ResponseEntity<PhotoViewDTO> delete_photo(@PathVariable long album_id, @PathVariable Long photo_id,
            Authentication authentication) {
        try {
            Account account = accountService.findByEmail(authentication.getName()).get();
            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();

                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Optional<Photo> optionalPhoto = photoService.findById(photo_id);
            if (optionalPhoto.isPresent()) {
                Photo photo = optionalPhoto.get();
                if (photo.getAlbum().getId() != album_id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                AppUtils.delete_photo_from_path(photo.getFileName(), PHOTOS_FOLDER_NAME, album_id);
                AppUtils.delete_photo_from_path(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);
                photoService.delete(photo);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // This method is used to download a photo or its thumbnail from an album
    // It requires authentication to access this endpoint
    // It returns the photo or thumbnail as a Resource
    // If the photo or thumbnail does not exist, it returns a 404 Not Found response
    @GetMapping(value = "/albums/{album_id}/photos/{photo_id}/download-photo")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @Operation(summary = "Download photo")
    public ResponseEntity<?> downloadPhoto(@PathVariable long album_id, @PathVariable long photo_id,
            Authentication authentication) {

        return downloadFile(album_id, photo_id, PHOTOS_FOLDER_NAME, authentication);
    }

    // This method is used to download a thumbnail of a photo from an album
    // It requires authentication to access this endpoint
    // It returns the thumbnail as a Resource
    // If the thumbnail does not exist, it returns a 404 Not Found response
    @GetMapping(value = "/albums/{album_id}/photos/{photo_id}/download-thumbnail")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @Operation(summary = "Download thumbnail")
    public ResponseEntity<?> downloadThumbnail(@PathVariable long album_id, @PathVariable long photo_id,
            Authentication authentication) {

        return downloadFile(album_id, photo_id, THUMBNAIL_FOLDER_NAME, authentication);
    }

    // This method is a helper method to download a file (photo or thumbnail) from
    // an album
    // It checks if the album and photo exist and belong to the authenticated user
    // If they do, it retrieves the file as a Resource and returns it with
    // appropriate headers
    public ResponseEntity<?> downloadFile(Long album_id, Long photo_id, String folder_name,
            Authentication authentication) {
        Account account = accountService.findByEmail(authentication.getName()).get();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();

            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            if (photo.getAlbum().getId() != album_id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            Resource resource = null;
            try {
                resource = AppUtils.getFileAsResource(album_id, PHOTOS_FOLDER_NAME, photo.getFileName());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

            if (resource == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }

            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

}
