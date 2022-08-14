package com.infomansion.server.domain.userstuff.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.payment.domain.Payment;
import com.infomansion.server.domain.payment.domain.PaymentLine;
import com.infomansion.server.domain.payment.repository.PaymentRepository;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.DefaultStuff;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.*;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserStuffServiceImpl implements UserStuffService {

    private final UserRepository userRepository;
    private final StuffRepository stuffRepository;
    private final UserStuffRepository userStuffRepository;
    private final PaymentRepository paymentRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public Long saveUserStuff(UserStuffSaveRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Stuff stuff = stuffRepository.findById(requestDto.getStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));

        return userStuffRepository.save(requestDto.toEntity(user, stuff)).getId();
    }

    @Override
    public List<UserStuffEditResponseDto> findAllUserStuff() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Category> categories = Arrays.asList(Category.GUESTBOOK, Category.POSTBOX);
        return userStuffRepository.findByUser(user).stream()
                .filter(userStuff -> userStuff.getCategory() == null || !categories.contains(userStuff.getCategory()))
                .map(UserStuffEditResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserStuffResponseDto findUserStuffByUserStuffId(Long userStuffId) {
        UserStuff findUserStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        return UserStuffResponseDto.toDto(findUserStuff);
    }

    @Transactional
    @Override
    public Long modifyAliasOrCategory(UserStuffModifyRequestDto requestDto) {
         // Alias와 Category 둘 다 입력하지 않았다면 throw
        if(requestDto.getCategory() == null && requestDto.getAlias() == null)
            throw new CustomException(ErrorCode.NULL_VALUE_OF_ALIAS_AND_CATEGORY);

        // category가 null이 아닐 경우 Category Enum에 존재하는 값인지 체크
        // Category가 null일 경우 Alias만 변경하는 경우이기 때문에 아무런 오류처리를 하지 않는다.
        requestDto.isValidEnum();

        UserStuff us = userStuffRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        // 배치되지 않은 UserStuff의 Alias나 Category를 변경할 경우 throw
        if(!us.getSelected()) throw new CustomException(ErrorCode.EXCLUDED_USER_STUFF);

        // category가 null이 아닐 경우 새로운 category로 변경할 수 있는지 검증
        if(requestDto.getCategory() != null) {
            checkDuplicatePlacedCategory(us.getUser().getId(), requestDto.getCategory());
            checkAcceptableCategory(us.getStuff(), requestDto.getCategory());
        }

        // category가 null일 경우 changeAliasOrCategory에서 변경이 일어나지 않고 기존 category를 사용한다.
        // alias가 null일 경우 changeAliasOrCategory에서 변경이 일어나지 않고 기존 alias를 사용한다.
        us.changeAliasOrCategory(requestDto.getAlias(), requestDto.getCategory());
        return us.getId();
    }

    @Transactional
    @Override
    public Long removeUserStuff(Long userStuffId) {
        UserStuff userStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
        UserStuff garbage = userStuffRepository.findUserStuffByStuffType(SecurityUtil.getCurrentUserId(), StuffType.POSTBOX)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        postRepository.movePostToAnotherStuff(userStuff, garbage);

        userStuff.deleteUserStuff();
        userStuffRepository.save(userStuff);
        return userStuffId;
    }

    @Override
    public List<UserStuffArrangedResponseDto> findArrangedUserStuffByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 내 방을 보는 것이 아닐 경우에 user의 공개 state가 'private'이라면 권한 없음.

        if(user.isPrivateFlag() && !user.getId().equals(SecurityUtil.getCurrentUserId())){
            throw new CustomException(ErrorCode.NOT_PUBLIC_USER);
        }
        List<Category> categories = Arrays.asList(Category.GUESTBOOK, Category.POSTBOX);
        return userStuffRepository.findArrangedByUser(user, categories).stream()
                .map(userStuff -> UserStuffArrangedResponseDto.toDto(userStuff))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveDefaultUserStuff(User user) {
        List<Stuff> defaultStuffs = stuffRepository.findStuffByIdIn(DefaultStuff.getDefaultStuffIds());
        for (Stuff defaultStuff : defaultStuffs) {
            UserStuff defaultUserStuff = DefaultStuff.getDefaultUserStuff(defaultStuff, user);
            userStuffRepository.save(defaultUserStuff);
        }
    }

    @Override
    @Transactional
    public List<StuffResponseDto> purchaseStuff(UserStuffPurchaseRequestDto requestDto) {
        User user = userRepository.findUserWithCredit(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Stuff> stuffList = stuffRepository.findStuffByIdIn(requestDto.getStuffIds());

        // 전체 가격을 계산 후 사용자가 가구목록을 구매할 수 있을 지 판단
        long totalPrice = 0L;
        for(Stuff stuff : stuffList) {
            totalPrice += stuff.getPrice();
        }

        // 구매목록을 한번에 살 수 없을 때
        if (user.getCredit() < totalPrice) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_CREDIT);
        }

        // 결제 시작
        Payment payment = Payment.record(user.getUserCredit(), totalPrice);
        for(Stuff stuff : stuffList) {
            user.purchaseStuff(stuff.getPrice());
            userStuffRepository.save(UserStuff.havePossession(stuff, user));
            payment.addPaymentLine(PaymentLine.createPaymentLine(payment, stuff, stuff.getPrice()));
        }

        paymentRepository.save(payment);

        return stuffList.stream().map(StuffResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<UserStuffCategoryResponseDto> findCategoryPlacedInRoom() {
        User loginUser = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Category> categories = Arrays.asList(Category.NONE, Category.GUESTBOOK);
        return userStuffRepository.findCategoryPlacedInRoom(loginUser, categories)
                .stream().map(UserStuffCategoryResponseDto::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean editUserStuff(List<UserStuffEditRequestDto> requestDtos) {
        User loginUser = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 배치할 UserStuffIds
        List<Long> placedUserStuffIds = requestDtos.stream().map(UserStuffEditRequestDto::getUserStuffId).collect(Collectors.toList());

        // 방에서 제외되어야 할 UserStuff들의 selected와 Position, Rotation만 변경
        List<Category> categories = Arrays.asList(Category.GUESTBOOK, Category.POSTBOX);
        List<UserStuff> excludedUserStuffs = userStuffRepository.findByUserIsAndIdNotInAndSelectedIsTrueAndCategoryNotIn(loginUser, placedUserStuffIds, categories);
        if(excludedUserStuffs.size() > 0) {
            excludedUserStuffs.forEach(userStuff -> userStuff.changeExcludedState());
        }

        Set<String> checkDuplicateCategory = new HashSet<>();
        userStuffRepository.findByIdIn(placedUserStuffIds).forEach(placedUserStuff -> {
            for (UserStuffEditRequestDto requestDto : requestDtos) {
                if(requestDto.getUserStuffId().equals(placedUserStuff.getId())) {
                    // 새롭게 배치할 UserStuff의 카테고리 중복 검사 및 Stuff에 적용가능한 지 검사
                    if(!requestDto.getSelectedCategory().equals("NONE") && checkDuplicateCategory.contains(requestDto.getSelectedCategory())) {
                        throw new CustomException(ErrorCode.DUPLICATE_CATEGORY);
                    }
                    checkAcceptableCategory(placedUserStuff.getStuff(), requestDto.getSelectedCategory());
                    checkDuplicateCategory.add(requestDto.getSelectedCategory());

                    placedUserStuff.changePlacedStatus(requestDto);
                    break;
                }
            }
        });

        // 배치에서 제외된 UserStuff에 종속된 Post들을 PostBox로 이동
        if(excludedUserStuffs.size() > 0) {
            UserStuff garbage = userStuffRepository.findUserStuffByStuffType(SecurityUtil.getCurrentUserId(), StuffType.POSTBOX)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
            postRepository.movePostToAnotherStuffs(excludedUserStuffs, garbage);
        }
        return true;
    }

    private void checkDuplicatePlacedCategory(Long userId, String category) {
        // NONE 카테고리는 중복배치가 가능할 수 있게 함
        if(category.equals("NONE")) return;
        if(userStuffRepository.findAllCategoryByUserId(userId).contains(category))
            throw new CustomException(ErrorCode.DUPLICATE_CATEGORY);
    }

    private void checkAcceptableCategory(Stuff stuff, String category) {
        if(!stuff.getCategoryList().contains(category))
            throw new CustomException(ErrorCode.UNACCEPTABLE_CATEGORY);
    }


}
