package com.example.demo.task.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskRepository;
import com.example.demo.task.service.base.BaseService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskService extends BaseService<TaskEntity, Long, TaskRepository> {

    public List<TaskEntity> findByParamsOrderedBy(final Optional<Integer> priority,
                                                  final Optional<Boolean> completed,
                                                  final Sort sort) {

        Specification<TaskEntity> specPriorityTask = new Specification<TaskEntity>() {

            @Override
            public Predicate toPredicate(Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (priority.isPresent()) {
                    return criteriaBuilder.equal(root.get("priorityValue"), priority.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // nothing to filter
                }
            }

        };

        Specification<TaskEntity> completedEqualsTo = new Specification<TaskEntity>() {

            @Override
            public Predicate toPredicate(Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (completed.isPresent()) {
                    return criteriaBuilder.equal(root.get("completed"), completed.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // nothing to filter
                }
            }


        };

        Specification<TaskEntity> allParams = specPriorityTask.and(completedEqualsTo);

        return this.repository.findAll(allParams, sort);

    }

}
