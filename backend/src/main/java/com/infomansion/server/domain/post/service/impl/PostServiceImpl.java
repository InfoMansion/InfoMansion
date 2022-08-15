package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.domain.PostImage;
import com.infomansion.server.domain.post.dto.*;
import com.infomansion.server.domain.post.repository.PostImageRepository;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.repository.UserLikePostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.FollowRepository;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final UserStuffRepository userStuffRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserLikePostRepository userLikePostRepository;
    private final FollowRepository followRepository;
    private final S3Uploader s3Uploader;

    private final Long postPublishingCredit = 30L;

    @Transactional
    @Override
    public Long createPost(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        List<String> deleteImages = requestDto.getImages()
                .stream().filter(image -> !requestDto.getContent().contains(image))
                .collect(Collectors.toList());
        if (deleteImages.size() > 0)
            s3Uploader.deleteFiles(deleteImages);

        return postRepository.save(requestDto.toEntity(user, userStuff)).getId();
    }

    @Override
    public PostbyUserStuffResponseDto findPostByUserStuffId(Long userStuffId, Pageable pageable) {

        UserStuff userStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        Slice<PostSimpleResponseDto> postsByUserStuff
                = postRepository.findByUserStuffId(userStuff, pageable)
                .map(PostSimpleResponseDto::new);

        return new PostbyUserStuffResponseDto(postsByUserStuff);
    }

    @Override
    public PostSearchResponseDto findPostBySearchWordForTitle(String searchWord, Pageable pageable) {

        if(searchWord==null || searchWord.length()==0) {
            throw new CustomException(ErrorCode.SEARCH_WORD_NOT_BLANK);
        }

        Slice<PostSimpleResponseDto> postsByTitle =
                postRepository.findByTitle(searchWord, pageable)
                        .map(PostSimpleResponseDto::new);
        return new PostSearchResponseDto(postsByTitle);
    }

    @Override
    public PostSearchResponseDto findPostBySearchWordForContent(String searchWord, Pageable pageable) {

        if(searchWord==null || searchWord.length()==0) {
            throw new CustomException(ErrorCode.SEARCH_WORD_NOT_BLANK);
        }

        Slice<PostSimpleResponseDto> postsByTitle =
                postRepository.findByContent(searchWord, pageable)
                        .map(PostSimpleResponseDto::new);
        return new PostSearchResponseDto(postsByTitle);
    }

    @Override
    public PostDetailResponseDto findPostWithUser(Long postId) {

        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Long userId = SecurityUtil.getCurrentUserId();

        return PostDetailResponseDto.toDto(post,
                followRepository.existsByFromUserIdAndToUserIs(userId, post.getUser()),
                userLikePostRepository.existsUserLikePostByPostAndUserId(post, userId)
        );
    }

    @Override
    public List<PostSimpleResponseDto> findRecent5ByUser(String userName, Pageable pageable) {
        int size = 5;
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();

        if(!user.getId().equals(SecurityUtil.getCurrentUserId()) && user.isPrivateFlag()){
            throw new CustomException(ErrorCode.NOT_PUBLIC_USER);
        }
        List<PostSimpleResponseDto> top5RecentPost = new ArrayList<>();
        postRepository.findTop5Post(userId, PageRequest.of(0,size))
                .forEach(post -> top5RecentPost.add(new PostSimpleResponseDto(post)));

        return top5RecentPost;
    }

    @Transactional
    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getUser().getId().equals(SecurityUtil.getCurrentUserId()) && !post.getUserStuff().getUser().getId().equals(SecurityUtil.getCurrentUserId()))
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);

        post.deletePost(s3Uploader);
        return true;
    }

    @Override
    public boolean modifyPostAndSaveAsTemp(PostModifyRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!post.getUserStuff().getId().equals(requestDto.getUserStuffId())) {
            UserStuff userStuffToMove = userStuffRepository.findById(requestDto.getUserStuffId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
            post.updatePostWithUserStuff(userStuffToMove, requestDto.getTitle(), requestDto.getContent());
        } else
            post.updatePost(requestDto.getTitle(), requestDto.getContent());

        List<String> deleteImages = requestDto.getImages()
                .stream().filter(image -> !requestDto.getContent().contains(image))
                .collect(Collectors.toList());
        if (deleteImages.size() > 0)
            s3Uploader.deleteFiles(deleteImages);

        return true;
    }

    /**
     * 새로 작성하는 글에 이미지를 업로드하는 경우, 임시 글을 생성하고 이미지를 업로드한다.
     * @param multipartFile
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public TempPostImageUploadResponseDto createTempPostAndUploadImage(MultipartFile multipartFile, TempPostSaveRequestDto requestDto) {

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.createTempPost(user, requestDto.getTitle(), requestDto.getContent());

        String imageUrl = null;
        try {
            imageUrl = post.uploadImage(multipartFile, s3Uploader, user.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postRepository.save(post);
        post.linkOriginalPost(post.getId());

        return TempPostImageUploadResponseDto.builder()
                .postId(post.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imgUrl(imageUrl)
                .build();
    }

    /**
     * 새로 작성하는 글을 임시 저장할 때
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public TempPostSaveResponseDto createTempPost(TempPostSaveRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.save(Post.createTempPost(user,requestDto.getTitle(), requestDto.getContent()));
        post.linkOriginalPost(post.getId());

        return TempPostSaveResponseDto.builder()
                .postId(post.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
    }

    /**
     * 새로 작성한 포스트를 발행
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public PostSaveResponseDto createNewPost(PostSaveRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        Post post = postRepository.save(Post.createPost(user, userStuff, requestDto.getTitle(), requestDto.getContent()));
        post.linkOriginalPost(post.getId());
        user.earnCredit(postPublishingCredit);

        return PostSaveResponseDto.builder()
                .userStuffId(requestDto.getUserStuffId())
                .postId(post.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
    }

    /**
     * 임시 포스트 또는 발행된 포스트를 수정하면서 이미지 업로드를 하는 경우
     * @param multipartFile
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public TempPostImageUploadResponseDto modifyPostAndImageUpload(MultipartFile multipartFile, TempPostSaveRequestDto requestDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(post.isPublished()) {
            // 발행된 포스트면 사본을 만들고 저장해둬야함
            Post tempPost = post.makeCopy();
            deleteImage(requestDto.getContent(), post);
            tempPost.updatePost(requestDto.getTitle(), requestDto.getContent());

            String imgUrl = null;
            try {
                imgUrl = tempPost.uploadImage(multipartFile, s3Uploader, user.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            postRepository.save(tempPost);

            return TempPostImageUploadResponseDto.builder()
                    .postId(tempPost.getId())
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .imgUrl(imgUrl)
                    .build();
        }

        // 임시저장 포스트일 때
        String imageUrl = null;
        try {
            imageUrl = post.uploadImage(multipartFile, s3Uploader, user.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return TempPostImageUploadResponseDto.builder()
                .postId(post.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imgUrl(imageUrl)
                .build();
    }

    /**
     * 임시 포스트 또는 발행된 포스트를 수정하고 다시 임시 저장
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public TempPostSaveResponseDto modifyPostAndSaveAsTemp(TempPostSaveRequestDto requestDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(post.isPublished()) {
            // 발행된 포스트일 때, 사본을 만들고 사본을 저장
            Post tempPost = post.makeCopy();
            deleteImage(requestDto.getContent(), tempPost);
            tempPost.updatePost(requestDto.getTitle(), requestDto.getContent());

            postRepository.save(tempPost);

            return TempPostSaveResponseDto.builder()
                    .postId(tempPost.getId())
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .build();
        }

        // 임시 저장된 포스트일 때 수정 후 저장
        deleteImage(requestDto.getContent(), post);
        post.updatePost(requestDto.getTitle(), requestDto.getContent());

        return TempPostSaveResponseDto.builder()
                .postId(post.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
    }

    /**
     * 임시 포스트를 발행하거나 발행된 포스트를 재 발행
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public PostSaveResponseDto publishPost(PostSaveRequestDto requestDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 1. Post 를 발행할 것이기 때문에 사본과 원본 상관없이 고아가 된 이미지 제거 & Post 의 제목과 내용 업데이트
        deleteImage(requestDto.getContent(), post);
        post.updatePost(requestDto.getTitle(), requestDto.getContent());

        if(!post.getOriginPostId().equals(post.getId())) {
            // 사본을 발행하는 경우, 고아가 된 이미지를 제거하고 제목과 내용이 업데이트된 사본을 기반으로 원본을 업데이트 후 발행
            // 원본을 발행한 후 사본 삭제
            Post original = postRepository.findById(post.getOriginPostId())
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

            original.updateOriginalByCopy(post);
            original.publish();

            original.changeUserStuff(userStuff);
            postRepository.delete(post);
            user.earnCredit(postPublishingCredit);

            return PostSaveResponseDto.builder()
                    .userStuffId(requestDto.getUserStuffId())
                    .postId(original.getId())
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .build();
        }

        // 원본을 발행하는 경우, 발행
        post.changeUserStuff(userStuff);
        post.publish();
        user.earnCredit(postPublishingCredit);

        return PostSaveResponseDto.builder()
                .userStuffId(requestDto.getUserStuffId())
                .postId(postId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
    }

    @Override
    public List<TempPostSaveResponseDto> findTempPosts() {
        return postRepository.findTempPostsByUserId(SecurityUtil.getCurrentUserId())
                .stream().map(TempPostSaveResponseDto::toDto).collect(Collectors.toList());
    }

    @Override
    public Slice<PostSimpleResponseDto> findPostInThePostbox(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserStuff postbox = userStuffRepository.findUserStuffByStuffType(user.getId(), StuffType.POSTBOX)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
        return postRepository.findByUserStuffId(postbox, pageable)
                .map(PostSimpleResponseDto::new);
    }

    @Override
    public Slice<PostGuestBookResponseDto> findPostInTheGuestbook(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserStuff guestbook = userStuffRepository.findUserStuffByStuffType(user.getId(), StuffType.GUESTBOOK)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
        return postRepository.findPostInTheGuestBook(guestbook, pageable)
                .map(PostGuestBookResponseDto::toDto);
    }

    @Transactional
    @Override
    public Long createGuestBookPost(String username, PostGuestBookRequestDto requestDto) {
        User loginUser = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserStuff guestbook = userStuffRepository.findUserStuffByStuffType(user.getId(), StuffType.GUESTBOOK)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
        return postRepository.save(Post.createPost(loginUser, guestbook, "", requestDto.getContent())).getId();
    }

    @Transactional
    @Override
    public boolean modifyGuestBookPost(PostGuestBookModifyRequestDto requestDto) {
        User loginUser = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!loginUser.getId().equals(post.getUser().getId())) throw new CustomException(ErrorCode.USER_NO_PERMISSION);

        post.updatePost("", requestDto.getContent());
        return true;
    }

    /**
     * Post가 가지고 있는 PostImage들의 주소와 client로부터 받아온 게시글 내용의 image url을 비교하여 삭제된 이미지들을 s3에서 삭제
     * @param content
     * @param post
     */
    private void deleteImage(String content, Post post) {
        List<String> deleteImages = extractImagesUrlFromContent(content).stream().filter(imageUrl -> !post.getImageUrls().contains(imageUrl))
                .collect(Collectors.toList());

        if(deleteImages.size() > 0) {
            List<PostImage> postImagesByImageUrlIsIn = postImageRepository.findPostImagesByImageUrlIsIn(deleteImages);

            for(PostImage postImage : postImagesByImageUrlIsIn) {
                post.deleteImage(postImage);
            }
            postImageRepository.deletePostImagesByIdIsIn(postImagesByImageUrlIsIn.stream().map(PostImage::getId).collect(Collectors.toList()));
            s3Uploader.deleteFiles(deleteImages);
        }
    }

    /**
     * client로부터 받아오는 게시글 내용에서 img src를 parsing해 url만 분리
     * @param content
     * @return
     */
    private List<String> extractImagesUrlFromContent(String content) {
        Document document = Jsoup.parseBodyFragment(content);
        Elements imgs = document.getElementsByTag("img");
        return imgs.stream().map(String::valueOf).collect(Collectors.toList());
    }

}
