package org.practice.SpringRestdemo.service;

import java.util.List;
import java.util.Optional;

import org.practice.SpringRestdemo.Models.Album;
import org.practice.SpringRestdemo.Repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public Album save(Album album) {
        return albumRepository.save(album);
    }

    public List<Album> findByAccount_id(Long id) {
        return albumRepository.findByAccount_id(id);
    }

    public Optional<Album> findById(Long album_id) {
        return albumRepository.findById(album_id);
    }

    public void deleteAlbum(Album album) {
        albumRepository.delete(album);

    }

}
