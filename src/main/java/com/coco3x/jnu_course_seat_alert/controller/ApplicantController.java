package com.coco3x.jnu_course_seat_alert.controller;

import com.coco3x.jnu_course_seat_alert.config.annotation.Session;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiMessage;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponse;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponseCreator;
import com.coco3x.jnu_course_seat_alert.domain.dto.layer.request.CourseCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.ApplicantCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import com.coco3x.jnu_course_seat_alert.service.ApplicantService;
import com.coco3x.jnu_course_seat_alert.service.CourseService;
import com.coco3x.jnu_course_seat_alert.service.UserService;
import com.coco3x.jnu_course_seat_alert.util.CourseValidationCrawler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applicant")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;
    private final CourseService courseService;
    private final UserService userService;
    private final CourseValidationCrawler courseValidationCrawler;

    @PostMapping()
    public ApiResponse<?> enrollApplicantInCourse(@Session(attr = "id") Long id, @RequestBody @Valid ApplicantCreateReqDTO applicantCreateReqDTO){
        Course course = courseService.findCourseByCode(applicantCreateReqDTO.getCode());
        if (course == null) {
            String realName = courseValidationCrawler.validate(applicantCreateReqDTO.getCode(), applicantCreateReqDTO.getName(), applicantCreateReqDTO.getGrade());
            courseService.createCourse(CourseCreateReqDTO.builder()
                    .code(applicantCreateReqDTO.getCode())
                    .grade(applicantCreateReqDTO.getGrade())
                    .name(realName)
                    .build());
        }

        User user = userService.findUserById(id);
        applicantService.enrollApplicantInCourse(applicantCreateReqDTO.getCourseType(), user, course);

        return ApiResponseCreator.success(new ApiMessage("-"));
    }
}
