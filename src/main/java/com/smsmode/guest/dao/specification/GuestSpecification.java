package com.smsmode.guest.dao.specification;

import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.GuestModel_;
import org.springframework.data.jpa.domain.Specification;

public class GuestSpecification {
    public static Specification<GuestModel> withIdEqual(String guestId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(GuestModel_.id), guestId);
    }

    public static Specification<GuestModel> withFirstNameContaining(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GuestModel_.firstName)),
                        "%" + firstName.toLowerCase() + "%"
                );
    }

    public static Specification<GuestModel> withLastNameContaining(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GuestModel_.lastName)),
                        "%" + lastName.toLowerCase() + "%"
                );
    }

    public static Specification<GuestModel> withEmailEqual(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(GuestModel_.contact).get("email"), email);
    }

    public static Specification<GuestModel> withSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + search.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(GuestModel_.firstName)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(GuestModel_.lastName)), likePattern)
            );
        };
    }
}