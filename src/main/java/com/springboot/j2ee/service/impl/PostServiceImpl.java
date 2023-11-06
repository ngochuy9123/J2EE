package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.FriendDTO;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EPostVisibility;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.FriendService;
import com.springboot.j2ee.service.PostService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendService friendService;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FriendService friendService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.friendService = friendService;
    }


    @Override
    public Post getInfoPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public List<Post> getAllPost(Long idCurrentUser) {
        List<Friend> lstFriend = friendService.displayListFriend(idCurrentUser);
        List<Long> lstIdFriend = new ArrayList<>();
        User user = userRepository.findById(idCurrentUser).get();

        for (Friend f:lstFriend) {
             if (!Objects.equals(f.getUserTo().getId(), idCurrentUser)){
                 lstIdFriend.add(f.getUserTo().getId());
             }else{
                 lstIdFriend.add(f.getUserFrom().getId());
             }

        }
        List<Post> lstPostFriend = new ArrayList<>();
        for (Long idUser:lstIdFriend
             ) {
            User user1 = userRepository.findById(idUser).get();

            List<Post> lsTemp = postRepository.findByUserAndVisibleOrderByCreatedAtDesc(user1, EPostVisibility.FRIENDS);
            lstPostFriend.addAll(lsTemp);
        }
//        System.out.println(lstPostFriend.size());
//        for (Post p:lstPostFriend
//             ) {
//            System.out.println(p.getContent());
//        }
        List<Post> lstPostPublic = postRepository.findByVisibleOrderByCreatedAtDesc(EPostVisibility.PUBLIC);

        lstPostPublic.addAll(lstPostFriend);

        lstPostPublic.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        return lstPostPublic;
    }

    @Override
    public List<Post> getPostByIdUser(Long id) {
        User user = userRepository.findById(id).get();
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Post> findPost(String filter) {

        return postRepository.findPostByContent(filter);
    }


}
