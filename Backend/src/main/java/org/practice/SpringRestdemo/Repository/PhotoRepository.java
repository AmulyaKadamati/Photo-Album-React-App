package org.practice.SpringRestdemo.Repository;

import java.util.List;
import java.util.Optional;

import org.practice.SpringRestdemo.Models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    public Optional<Photo> findById(Long id);

    public List<Photo> findByAlbum_id(Long id);

}
