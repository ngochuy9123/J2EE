package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.entity.Like;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.LikeRepository;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.LikeService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    public LikeServiceImpl(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Like saveLike(LikeDTO likeDTO) {

        Like l = new Like();

        l.setPostEmote(postRepository.findById(likeDTO.getIdPost()).get());
        l.setUserEmote(userRepository.findById(likeDTO.getIdUser()).get());
        l.setCreatedAt(timestamp);

        return likeRepository.save(l);
    }

    @Override
    public void disLike(Long id) {

        likeRepository.deleteLikeById(id);

    }

    @Override
    public Like findLike(LikeDTO likeDTO) {
        Post post = postRepository.findById(likeDTO.getIdPost()).get();
        User user = userRepository.findById(likeDTO.getIdUser()).get();
        return likeRepository.findByPostEmoteAndUserEmote(post,user);
    }

    @Override
    public List<Like> getAllEmoteByPostID(Post post) {

        return likeRepository.findByPostEmote(post);
    }

    @Override
    public long getAllLikeByPostId(Post post) {

        return likeRepository.countByPostEmote(post);
    }
}
