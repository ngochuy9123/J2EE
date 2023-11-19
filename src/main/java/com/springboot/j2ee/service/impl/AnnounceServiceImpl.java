package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.AnnounceDTO;
import com.springboot.j2ee.entity.Announce;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EAnnounceStatus;
import com.springboot.j2ee.enums.EAnnounceType;
import com.springboot.j2ee.repository.AnnounceRepository;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.AnnounceService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AnnounceServiceImpl implements AnnounceService {
    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public AnnounceServiceImpl(AnnounceRepository announceRepository, UserRepository userRepository, PostRepository postRepository) {
        this.announceRepository = announceRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @Override
    public void changeToStatusSeen(long idUserTo) {
        User userTo = userRepository.findById(idUserTo).get();
        List<Announce> lstAnnounce = announceRepository.findByUserToOrderByCreateAtDesc(userTo);

        for (Announce announce:lstAnnounce) {
            if (announce.getEAnnounceStatus() == EAnnounceStatus.NONE){
                announce.setEAnnounceStatus(EAnnounceStatus.SEEN);
                announceRepository.save(announce);
            }
            else{
                break;
            }
        }

    }

    @Override
    public Announce addAnnounce(AnnounceDTO announceDTO) {

        User userFrom = userRepository.findById(announceDTO.getIdUserFrom()).get();
        User userTo = userRepository.findById(announceDTO.getIdUserTo()).get();
        Post post = postRepository.findById(announceDTO.getIdPost()).get();

        Announce announce = new Announce();
        announce.setEAnnounceType(announceDTO.getEAnnounceType());
        announce.setEAnnounceStatus(announceDTO.getEAnnounceStatus());
        announce.setPost(post);
        announce.setCreateAt(announceDTO.getCreat_at());
        announce.setUserFrom(userFrom);
        announce.setUserTo(userTo);


        return announceRepository.save(announce);
    }

    @Override
    public void removeAnnounce(AnnounceDTO announceDTO) {
        User userTo = userRepository.findById(announceDTO.getIdUserTo()).get();
        User userFrom = userRepository.findById(announceDTO.getIdUserFrom()).get();
        Post post = postRepository.findById(announceDTO.getIdPost()).get();

        List<Announce> lstAnnounce = announceRepository.findByUserToAndUserFromAndPost(userTo,userFrom,post);

        for (Announce announce:lstAnnounce
             ) {
            System.out.println(announce.getUserFrom().getEmail());
            System.out.println(announce.getCreateAt());
            System.out.println(announce.getId());
            System.out.println(announce.getPost().getContent());
            System.out.println(announce.getUserTo().getEmail());
            System.out.println(announce.getEAnnounceType());
        }



        announceRepository.delete(lstAnnounce.get(0));

    }

    @Override
    public List<Announce> getAnnounceByIdUser(long idUserTo) {
        User userTo = userRepository.findById(idUserTo).get();
        return announceRepository.findByUserToOrderByCreateAtDesc(userTo);
    }

}
