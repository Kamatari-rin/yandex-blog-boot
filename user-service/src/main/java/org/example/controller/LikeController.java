package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.dto.LikeStatusDTO;
import org.example.enums.LikeTargetType;
import org.example.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countLikes(
            @RequestParam Long targetId,
            @RequestParam LikeTargetType targetType) {
        int count = likeService.countLikesByIdAndTarget(targetId, targetType);
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<LikeDTO> saveOrUpdateLike(@RequestBody @Valid CreateLikeDTO createLikeDTO) {
        System.out.println("Прием лайка: " + createLikeDTO.toString());
        LikeDTO likeDTO = likeService.saveOrUpdateLike(createLikeDTO);
        System.out.println("Ответ лайк: " + likeDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(likeDTO);
    }

    @GetMapping("/posts/statuses")
    public ResponseEntity<List<LikeStatusDTO>> getPostLikesStatuses(
            @RequestParam List<Long> postIds,
            @RequestParam Long userId) {

        List<LikeStatusDTO> statuses = likeService.getLikesStatuses(postIds, userId, LikeTargetType.POST);
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/comments/statuses")
    public ResponseEntity<List<LikeStatusDTO>> getCommentLikesStatuses(
            @RequestParam List<Long> commentIds,
            @RequestParam Long userId) {

        List<LikeStatusDTO> statuses = likeService.getLikesStatuses(commentIds, userId, LikeTargetType.COMMENT);
        return ResponseEntity.ok(statuses);
    }
}
