package org.practice.SpringRestdemo.service;

import java.util.List;
import java.util.Optional;

import org.practice.SpringRestdemo.Models.Photo;
import org.practice.SpringRestdemo.Repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public Optional<Photo> findById(long photo_id) {
        return photoRepository.findById(photo_id);
    }

    public List<Photo> findByAlbum_id(Long album_id) {
        return photoRepository.findByAlbum_id(album_id);
    }

    public void delete(Photo photo) {
        photoRepository.delete(photo);
    }

}
