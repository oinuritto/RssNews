package ru.itis.rssnews.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.rssnews.dto.LikeDto;
import ru.itis.rssnews.services.LikesService;
import ru.itis.rssnews.services.UsersService;

import java.util.List;

@Tags(value = {
        @Tag(name = "Likes")
})
@Schema(description = "Work with likes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {
    private final LikesService likesService;
    private final UsersService usersService;

    @Operation(summary = "Like adding")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Like created"),
            @ApiResponse(responseCode = "401", description = "Request from unauthorized user"),
    })
    @PostMapping("/add/{articleId}")
    public ResponseEntity<?> createLike(Authentication authentication, @PathVariable Long articleId) {
        // проверяем, что пользователь авторизован
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println(authentication.getName());
            // добавляем лайк и возвращаем успешный ответ
            likesService.addLike(articleId, usersService.getCurrentUser().getId());
            return ResponseEntity.accepted().build();
        } else {
            // если пользователь не авторизован, возвращаем ошибку 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get all likes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page with likes",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LikeDto.class))
                    }
            )
    })
    @GetMapping()
    public ResponseEntity<List<LikeDto>> getAllLikes() {
        List<LikeDto> likes = likesService.getAllLikes();
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @Operation(summary = "Get all likes of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page with likes",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LikeDto.class))
                    }
            )
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeDto>> getLikesByUserId(@PathVariable Long userId) {
        List<LikeDto> likes = likesService.getLikesByUserId(userId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @Operation(summary = "Get all likes of article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page with likes",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LikeDto.class))
                    }
            )
    })
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<LikeDto>> getLikesByArticleId(@PathVariable Long articleId) {
        List<LikeDto> likes = likesService.getLikesByArticleId(articleId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @Operation(summary = "Get count of user's likes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Likes count",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))
                    }
            )
    })
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Integer> getLikesCountByUserId(@PathVariable Long userId) {
        int count = likesService.getLikesCountByUserId(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @Operation(summary = "Get count of article's likes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Likes count",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))
                    }
            )
    })
    @GetMapping("/article/{articleId}/count")
    public ResponseEntity<Integer> getLikesCountByArticleId(@PathVariable Long articleId) {
        int count = likesService.getLikesCountByArticleId(articleId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @Operation(summary = "Get all likes of article from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page with likes",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LikeDto.class))
                    }
            )
    })
    @GetMapping("/article/{articleId}/user/{userId}")
    public ResponseEntity<LikeDto> getLikeByArticleIdAndUserId(@PathVariable Long articleId, @PathVariable Long userId) {
        LikeDto like = likesService.getLikeByArticleIdAndUserId(articleId, userId);
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    @Operation(summary = "Delete like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Like deleted"),
            @ApiResponse(responseCode = "401", description = "Request from unauthorized user")
    })
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<?> deleteLikeByArticleId(@PathVariable Long articleId, Authentication authentication) {
        // проверяем, что пользователь авторизован
        if (authentication != null && authentication.isAuthenticated()) {
            // удаляем лайк и возвращаем успешный ответ
            likesService.deleteLikeByArticleIdAndUserId(articleId, usersService.getCurrentUser().getId());
            return ResponseEntity.accepted().build();
        } else {
            // если пользователь не авторизован, возвращаем ошибку 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
