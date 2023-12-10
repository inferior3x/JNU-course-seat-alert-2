package com.coco3x.jnu_course_seat_alert.service;

import com.coco3x.jnu_course_seat_alert.domain.dto.layer.request.CourseCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.response.CourseResDTO;
import com.coco3x.jnu_course_seat_alert.domain.entity.Applicant;
import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import com.coco3x.jnu_course_seat_alert.repository.ApplicantRepository;
import com.coco3x.jnu_course_seat_alert.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final ApplicantRepository applicantRepository;

    @Transactional(readOnly = true)
    public List<CourseResDTO> findAppliedCourses(User user){
        List<Applicant> applicants = applicantRepository.findApplicantsByUser(user);
        return applicants.stream()
                .map(applicant -> applicant.getCourse().toDTO(applicant.getCourseType()))
                .toList();
    }


    @Transactional(readOnly = true)
    public Course findCourseByCode(String code){
        return courseRepository.findCourseByCode(code).orElse(null);
    }

    public void createCourse(CourseCreateReqDTO courseCreateReqDTO){
        Course course = courseCreateReqDTO.toEntity();
        courseRepository.save(course);
    }

}
