package org.practice.SpringRestdemo.Repository;

import java.util.List;

import org.practice.SpringRestdemo.Models.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    public List<Album> findByAccount_id(Long id);

}
