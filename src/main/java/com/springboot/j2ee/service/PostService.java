package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.PostInfoDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.enums.EFriendRequest;
import com.springboot.j2ee.enums.EPostVisibility;

import java.util.List;

public interface PostService {
    Post getInfoPost(Long id);
    List<Post> getAllPost(Long id);

    List<Post> getAllPostByIdUserAndVisible(Long idUser, EPostVisibility ePostVisibility);

    List<Post> getAllPostHome(Long id);
    List<Post> getPostByIdUser(Long id);

    List<Post> findPost(String filter);
    PostInfoDTO getOnePost(Long id,Long idUser);

    List<Post> getAllPostFilter(Long id, String filter);

    List<Post> getAllPostForProfile(Long idUser, Long idCurrentUser);

}
