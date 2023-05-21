package ru.itis.rssnews.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ru.itis.rssnews.dto.ErrorResponse;
import ru.itis.rssnews.exceptions.NotFoundException;
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
    private static final String UNAUTHORIZED_MESSAGE = "Unauthorized";

    @Operation(summary = "Like adding")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Like created"),
            @ApiResponse(responseCode = "401", description = "Request from unauthorized user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Article for like not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/add/{articleId}")
    public ResponseEntity<?> createLike(Authentication authentication,
                                        @Parameter(description = "Article's id") @PathVariable Long articleId) {
        // проверяем, что пользователь авторизован
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                likesService.addLike(articleId, usersService.getCurrentUser().getId());
                return ResponseEntity.accepted().build();
            } catch (NotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message(e.getMessage())
                                .build());
            }
        } else {
            // если пользователь не авторизован, возвращаем ошибку 401
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(UNAUTHORIZED_MESSAGE)
                            .build());
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
    public ResponseEntity<List<LikeDto>> getLikesByUserId(@Parameter(description = "User's id") @PathVariable Long userId) {
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
    public ResponseEntity<List<LikeDto>> getLikesByArticleId(@Parameter(description = "Article's id") @PathVariable Long articleId) {
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
    public ResponseEntity<Integer> getLikesCountByUserId(@Parameter(description = "User's id") @PathVariable Long userId) {
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
    public ResponseEntity<Integer> getLikesCountByArticleId(@Parameter(description = "Article's id") @PathVariable Long articleId) {
        int count = likesService.getLikesCountByArticleId(articleId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @Operation(summary = "Get like of article from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LikeDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Like not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @GetMapping("/article/{articleId}/user/{userId}")
    public ResponseEntity<?> getLikeByArticleIdAndUserId(@Parameter(description = "Article's id") @PathVariable Long articleId,
                                                         @Parameter(description = "User's id") @PathVariable Long userId) {


        try {
            LikeDto like = likesService.getLikeByArticleIdAndUserId(articleId, userId);
            return new ResponseEntity<>(like, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Like deleted"),
            @ApiResponse(responseCode = "401", description = "Request from unauthorized user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not found liked article", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<?> deleteLikeByArticleId(@Parameter(description = "Article's id") @PathVariable Long articleId,
                                                   Authentication authentication) {
        // проверяем, что пользователь авторизован
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                likesService.deleteLikeByArticleIdAndUserId(articleId, usersService.getCurrentUser().getId());
                return ResponseEntity.accepted().build();
            } catch (NotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message(e.getMessage())
                                .build());
            }
        } else {
            // если пользователь не авторизован, возвращаем ошибку 401
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(UNAUTHORIZED_MESSAGE)
                            .build());
        }
    }
}
