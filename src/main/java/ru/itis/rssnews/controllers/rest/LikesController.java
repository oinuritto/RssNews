package ru.itis.rssnews.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.rssnews.dto.LikeDto;
import ru.itis.rssnews.services.LikesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/add")
    public ResponseEntity<?> createLike(@RequestBody LikeDto likeDto) {
        likesService.addLike(likeDto.getArticleId(), likeDto.getUserId());
        return ResponseEntity.accepted().build();
    }

    @GetMapping()
    public ResponseEntity<List<LikeDto>> getAllLikes() {
        List<LikeDto> likes = likesService.getAllLikes();
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeDto>> getLikesByUserId(@PathVariable Long userId) {
        List<LikeDto> likes = likesService.getLikesByUserId(userId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<LikeDto>> getLikesByArticleId(@PathVariable Long articleId) {
        List<LikeDto> likes = likesService.getLikesByArticleId(articleId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Integer> getLikesCountByUserId(@PathVariable Long userId) {
        int count = likesService.getLikesCountByUserId(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}/count")
    public ResponseEntity<Integer> getLikesCountByArticleId(@PathVariable Long articleId) {
        int count = likesService.getLikesCountByArticleId(articleId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}/user/{userId}")
    public ResponseEntity<LikeDto> getLikeByArticleIdAndUserId(@PathVariable Long articleId, @PathVariable Long userId) {
        LikeDto like = likesService.getLikeByArticleIdAndUserId(articleId, userId);
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    @DeleteMapping("/article/{articleId}/user/{userId}")
    public ResponseEntity<?> deleteLikeByArticleIdAndUserId(@PathVariable Long articleId, @PathVariable Long userId) {
        likesService.deleteLikeByArticleIdAndUserId(articleId, userId);
        return ResponseEntity.accepted().build();
    }
}
